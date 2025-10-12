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
        val factoryHint = property.factoryHint
        return if (factoryHint == Nothing::class) {
            try {
                ContainerRegistry.getFactory(property.component)
            } catch (e: IllegalStateException) {
                logger.warn("Factory for ${property.component} not found, using NoopContainer as fallback")
                fallbackContainer()
            }
        } else {
            ContainerRegistry.getFactoryOrAdd(property.component, property.factoryHint.createInstance())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> fallbackContainer(): Container<T> {
        return NoopContainer() as Container<T>
    }
}