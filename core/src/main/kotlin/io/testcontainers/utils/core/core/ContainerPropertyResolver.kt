package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import io.testcontainers.utils.core.injectable.PropertyInjector
import org.testcontainers.containers.GenericContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal object ContainerPropertyResolver {
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun resolveFactory(property: ContainerProperty): ContainerConfiguration<out GenericContainer<*>> {
        return getOrAdd(property)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> resolveCustomizer(customizerClass: KClass<out ContainerCustomizer<*>>): ContainerCustomizer<T> {
        return customizerClass.createInstance() as ContainerCustomizer<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> resolveInjectable(injectableClass: KClass<out PropertyInjector<*>>): PropertyInjector<T> {
        return injectableClass.createInstance() as PropertyInjector<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getOrAdd(property: ContainerProperty): ContainerConfiguration<out GenericContainer<*>> {
        val factoryHint = property.factoryHint
        return if (factoryHint == Nothing::class) {
            try {
                if (property.value.isBlank()) ContainerRegistry.getConfiguration(property.component)
                else ContainerRegistry.getConfiguration(property.value)
            } catch (e: IllegalStateException) {
                logger.warn("Factory for ${property.component} not found, using NoopContainer as fallback")
                fallbackContainer(property)
            }
        } else {
            val factory = factoryHint.createInstance() as Container<GenericContainer<*>>
            val configuration =
                factory.containerShell(
                    property.image,
                    property.recyle,
                    resolveCustomizer(property.customizer),
                    resolveInjectable(property.injectable)
                )
            val key = property.value.takeIf { it.isNotBlank() } ?: configuration.key()
            ContainerRegistry.register(key, configuration)
        }
    }

    private fun fallbackContainer(property: ContainerProperty): ContainerConfiguration<GenericContainer<*>> {
        val image = property.image
        val customizer = resolveCustomizer(property.customizer)
        return NoopContainer().containerShell(
            image,
            property.recyle,
            customizer,
            resolveInjectable(property.injectable)
        )
    }
}