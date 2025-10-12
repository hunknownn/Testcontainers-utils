package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.testcontainers.containers.GenericContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal object ContainerPropertyResolver {
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun resolveFactory(property: ContainerProperty): Container<GenericContainer<*>> {
        return getOrAdd(property)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : GenericContainer<*>> resolveCustomizer(customizerClass: KClass<out ContainerCustomizer<*>>): ContainerCustomizer<T> {
        return customizerClass.createInstance() as ContainerCustomizer<T>
    }

    private fun <T : GenericContainer<*>> getOrAdd(property: ContainerProperty): Container<T> {
        val factoryHint = property.factory
        return if (factoryHint == Nothing::class) {
            try {
                ContainerRegistry.getFactory(property.component)
            } catch (e: IllegalStateException) {
                logger.warn("Factory for ${property.component} not found, attempting to load default factory")
                loadDefaultFactory(property.component)
            }
        } else {
            ContainerRegistry.getFactoryOrAdd(property.component, property.factory.createInstance())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> loadDefaultFactory(component: Component): Container<T> {
        return when (component) {
            Component.POSTGRESQL -> {
                val factoryClass = Class.forName("io.testcontainers.utils.postgresql.PostgresContainerFactory")
                val factory = factoryClass.getDeclaredConstructor().newInstance() as Container<T>
                ContainerRegistry.register(component, factory)
                logger.info("Dynamically loaded and registered factory for $component")
                factory
            }

            else -> throw IllegalStateException("No default factory available for $component")
        }
    }
}