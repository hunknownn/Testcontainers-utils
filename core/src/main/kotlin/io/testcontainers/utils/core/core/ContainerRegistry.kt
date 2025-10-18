package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.annotation.ConflictingContainerConfigurationDefinitionException
import org.testcontainers.containers.GenericContainer

object ContainerRegistry {
    private val components: MutableMap<Component, String> = mutableMapOf()
    private val configurations: MutableMap<String, ContainerConfiguration<out GenericContainer<*>>> = mutableMapOf()

    internal fun register(
        component: Component,
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ) {
        if (components.containsKey(component) || configurations.containsKey(key)) throw ConflictingContainerConfigurationDefinitionException()

        this.components[component] = key
        this.configurations[key] = configuration
    }

    fun register(
        key: String,
        configuration: ContainerConfiguration<out GenericContainer<*>>
    ): ContainerConfiguration<out GenericContainer<*>> {
        if (configurations.containsKey(key)) {
            throw ConflictingContainerConfigurationDefinitionException()
        }
        this.configurations[key] = configuration
        return configuration
    }

    fun getConfiguration(component: Component): ContainerConfiguration<out GenericContainer<*>> {
        val key = components[component] ?: error("No configuration registered for component $component")
        return getConfiguration(key)
    }

    fun getConfiguration(key: String): ContainerConfiguration<out GenericContainer<*>> {
        return configurations[key] ?: error("No configuration registered for $key")
    }

    fun getConfigurations(): Map<String, ContainerConfiguration<out GenericContainer<*>>> {
        return configurations.toMap()
    }
}