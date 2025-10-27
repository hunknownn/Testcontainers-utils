package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import io.testcontainers.utils.core.injectable.ContainerPropertyInjector
import org.testcontainers.containers.GenericContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal object ContainerPropertyResolver {
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun resolveFactory(property: ContainerProperty): Pair<String, ContainerConfiguration<out GenericContainer<*>>> {
        return getOrAdd(property)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getOrAdd(property: ContainerProperty): Pair<String, ContainerConfiguration<out GenericContainer<*>>> {
        val factoryHint = property.factoryHint
        return if (factoryHint == Nothing::class) {
            try {
                if (property.value.isBlank()) ContainerRegistry.getConfiguration(property.component)
                else Pair(property.value, ContainerRegistry.getConfiguration(property.value))
            } catch (e: IllegalStateException) {
                logger.warn("Factory for ${property.component} not found, using NoopContainer as fallback")
                fallbackContainer(property)
            }
        } else {
            val factory = factoryHint.createInstance() as Container<GenericContainer<*>>
            val configuration =
                factory.containerShell(
                    property.image,
                    property.recycle,
                    resolveCustomizer(property.customizer),
                    resolveInjectable(property.injectable)
                )
            val key = configuration.key(property.value)
            return when (property.recycle) {
                Recycle.MERGE -> {
                    Pair(key, ContainerRegistry.registerMerge(key, configuration))
                }
                Recycle.NEW -> {
                    Pair(key, ContainerRegistry.registerNewly(key, configuration))
                }
            }
        }
    }

    private fun fallbackContainer(property: ContainerProperty): Pair<String, ContainerConfiguration<GenericContainer<*>>> {
        val image = property.image
        val customizer = resolveCustomizer(property.customizer)
        val configuration =  NoopContainer().containerShell(
            image,
            property.recycle,
            customizer,
            resolveInjectable(property.injectable)
        )
        return Pair(configuration.key(image), configuration)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> resolveCustomizer(customizerClass: KClass<out ContainerCustomizer<*>>): ContainerCustomizer<T> {
        return customizerClass.createInstance() as ContainerCustomizer<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> resolveInjectable(injectableClass: KClass<out ContainerPropertyInjector<*>>): ContainerPropertyInjector<T> {
        return injectableClass.createInstance() as ContainerPropertyInjector<T>
    }
}