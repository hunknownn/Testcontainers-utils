package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.annotation.ConflictingContainerConfigurationDefinitionException
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer

/**
 * Manages component to key mappings.
 */
internal object ComponentRegistry {
    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }
    private val components: MutableMap<Component, String> = mutableMapOf()

    fun register(component: Component, key: String) {
        if (components.containsKey(component)) {
            throw ConflictingContainerConfigurationDefinitionException()
        }
        components[component] = key
        logger.debug("Registered component {} with key {}", component, key)
    }

    fun getKey(component: Component): String? = components[component]

    fun containsComponent(component: Component): Boolean = components.containsKey(component)
}

/**
 * Manages container configurations storage.
 */
internal object ConfigurationRepository {
    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }
    private val configurations: MutableMap<String, ContainerConfiguration<out GenericContainer<*>>> = mutableMapOf()

    fun register(key: String, configuration: ContainerConfiguration<out GenericContainer<*>>) {
        if (configurations.containsKey(key)) {
            throw ConflictingContainerConfigurationDefinitionException()
        }
        configurations[key] = configuration
        logger.debug("Registered configuration with key {}", key)
    }

    fun registerOrGet(key: String, configuration: ContainerConfiguration<out GenericContainer<*>>): ContainerConfiguration<out GenericContainer<*>> {
        return configurations.getOrPut(key) {
            logger.debug("Registered new configuration with key {}", key)
            configuration
        }
    }

    fun get(key: String): ContainerConfiguration<out GenericContainer<*>>? = configurations[key]

    fun getAll(): Map<String, ContainerConfiguration<out GenericContainer<*>>> = configurations.toMap()

    fun containsKey(key: String): Boolean = configurations.containsKey(key)
}

/**
 * Manages container lifecycle and recycling.
 */
internal object ContainerLifecycleManager {
    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }
    private val headContainers: MutableMap<String, GenericContainer<*>> = mutableMapOf()
    private val containersByHash: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val keyToHash: MutableMap<String, String> = mutableMapOf()

    fun startNew(hash: String, key: String, container: GenericContainer<*>): GenericContainer<*> {
        headContainers[hash]?.let {
            logger.warn("Container with hash {} already exists for key {}", hash, key)
        }
        headContainers[hash] = container
        keyToHash[key] = hash
        return container
    }

    fun startOrReuse(hash: String, key: String, containerProvider: () -> GenericContainer<*>): GenericContainer<*>? {
        return headContainers[hash]?.let {
            logger.debug("Reusing existing container for hash {}", hash)
            registerForRecycling(hash, key)
            null
        } ?: run {
            logger.debug("Starting new container for hash {} with key {}", hash, key)
            startNew(hash, key, containerProvider())
        }
    }

    private fun registerForRecycling(hash: String, key: String) {
        containersByHash.getOrPut(hash) { mutableListOf() }.add(key)
        keyToHash[key] = hash
    }

    fun getHeadContainers(): Map<String, GenericContainer<*>> = headContainers.toMap()
}

/**
 * Facade for container registry operations.
 * Delegates to specialized registries for separation of concerns.
 */
object ContainerRegistry {
    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    internal fun register(
        component: Component,
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ) {
        if (ComponentRegistry.containsComponent(component) || ConfigurationRepository.containsKey(key)) {
            throw ConflictingContainerConfigurationDefinitionException()
        }
        ComponentRegistry.register(component, key)
        ConfigurationRepository.register(key, configuration)
        logger.debug("Registered container configuration for component {} with key {}", component, key)
    }

    fun registerNewly(
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ): ContainerConfiguration<out GenericContainer<*>> {
        ConfigurationRepository.register(key, configuration)
        return configuration
    }

    fun registerMerge(
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ): ContainerConfiguration<out GenericContainer<*>> {
        return ConfigurationRepository.registerOrGet(key, configuration)
    }

    fun getConfiguration(component: Component): Pair<String, ContainerConfiguration<out GenericContainer<*>>> {
        val key = ComponentRegistry.getKey(component)
            ?: error("No configuration registered for component $component".also { logger.error(it) })
        return key to getConfiguration(key)
    }

    fun getConfiguration(key: String): ContainerConfiguration<out GenericContainer<*>> {
        return ConfigurationRepository.get(key)
            ?: error("No configuration registered for $key")
    }

    fun getConfigurations(): Map<String, ContainerConfiguration<out GenericContainer<*>>> {
        return ConfigurationRepository.getAll()
    }

    fun newlyStart(hash: String, key: String, container: GenericContainer<*>): GenericContainer<*> {
        return ContainerLifecycleManager.startNew(hash, key, container)
    }

    fun mergeStart(hash: String, key: String): GenericContainer<*>? {
        return ContainerLifecycleManager.startOrReuse(hash, key) {
            getConfiguration(key).container
        }
    }

    fun getHeadContainers(): Map<String, GenericContainer<*>> {
        return ContainerLifecycleManager.getHeadContainers()
    }
}