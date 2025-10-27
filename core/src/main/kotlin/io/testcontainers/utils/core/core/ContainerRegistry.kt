package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.annotation.ConflictingContainerConfigurationDefinitionException
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer

object ContainerRegistry {

    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    private val components: MutableMap<Component, String> = mutableMapOf()
    private val configurations: MutableMap<String, ContainerConfiguration<out GenericContainer<*>>> = mutableMapOf()
    private val headContainers: MutableMap<String, GenericContainer<*>> = mutableMapOf()
    private val recyclingContainers: MutableMap<String, MutableList<String>> = mutableMapOf() // hash -> keys
    private val keyToHashes: MutableMap<String, String> = mutableMapOf()

    internal fun register(
        component: Component,
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ) {
        if (components.containsKey(component) || configurations.containsKey(key)) throw ConflictingContainerConfigurationDefinitionException()

        this.components[component] = key
        this.configurations[key] = configuration

        logger.debug("Registered container configuration for component $component with key $key")
    }

    fun registerNewly(
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ): ContainerConfiguration<out GenericContainer<*>> {
        if (configurations.containsKey(key)) {
            throw ConflictingContainerConfigurationDefinitionException()
        }
        logger.debug("Registered container configuration with key $key")
        this.configurations[key] = configuration
        return configuration
    }

    fun registerMerge(
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ): ContainerConfiguration<out GenericContainer<*>> {
        return configurations.getOrElse(key) {
            registerNewly(key, configuration)
        }
    }

    fun getConfiguration(component: Component): Pair<String, ContainerConfiguration<out GenericContainer<*>>> {
        val key = components[component] ?: run {
            logger.error("No configuration registered for component $component")
            error("No configuration registered for component $component")
        }
        return Pair(key, getConfiguration(key))
    }

    fun getConfiguration(key: String): ContainerConfiguration<out GenericContainer<*>> {
        return configurations[key] ?: error("No configuration registered for $key")
    }

    fun getConfigurations(): Map<String, ContainerConfiguration<out GenericContainer<*>>> {
        return configurations.toMap()
    }

    fun newlyStart(hash: String, key: String, container: GenericContainer<*>): GenericContainer<*> {
        headContainers[hash]?.let {
            logger.warn(
                "Container with hash {} already exists for key {}: {}",
                hash,
                key,
                it
            )
        }
        headContainers[hash] = container
        keyToHashes[key] = hash
        return container
    }

    fun mergeStart(hash: String, key: String): GenericContainer<*>? {
        logger.debug(
            "Checking for recycling container for key {} with hash {}, configurations: {}, headContainers: {}, recyclingContainers: {}",
            key,
            hash,
            configurations.toString(),
            headContainers.toString(),
            recyclingContainers.toString()
        )
        val head = headContainers[hash]
        if (head == null) { // hash로 시작된 컨테이너가 없다
            logger.debug("No existing container found for hash {}. Starting new container for key {}", hash, key)
            return newlyStart(hash, key, getConfiguration(key).container)
        } else { // hash로 시작된 컨테이너가 있다
            logger.debug("Found existing container for hash {}: {}", hash, head)
            val keys = recyclingContainers.getOrPut(hash) { mutableListOf() }
            keys.add(key)
            keyToHashes[key] = hash
            return null
        }
    }

    fun getHeadContainers(): Map<String, GenericContainer<*>> {
        return headContainers.toMap()
    }
}