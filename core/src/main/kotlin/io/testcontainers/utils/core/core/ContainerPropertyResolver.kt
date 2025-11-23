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

    private fun getOrAdd(property: ContainerProperty): Pair<String, ContainerConfiguration<out GenericContainer<*>>> {
        val factoryHint = property.factoryHint
        return if (factoryHint == Nothing::class) {
            try {
                if (property.value.isBlank()) ContainerRegistry.getConfiguration(property.component)
                else property.value to ContainerRegistry.getConfiguration(property.value)
            } catch (e: IllegalStateException) {
                logger.warn("Factory for ${property.component} not found, using NoopContainer as fallback")
                fallbackContainer(property)
            }
        } else {
            val factory = createContainerFactory(factoryHint)
            val configuration = factory.containerShell(
                property.image,
                property.recycle,
                resolveCustomizer(property.customizer),
                resolveInjectable(property.injectable)
            )
            val key = configuration.key(property.value)
            key to when (property.recycle) {
                Recycle.MERGE -> ContainerRegistry.registerMerge(key, configuration)
                Recycle.NEW -> ContainerRegistry.registerNewly(key, configuration)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createContainerFactory(factoryHint: kotlin.reflect.KClass<*>): Container<GenericContainer<*>> {
        return runCatching {
            factoryHint.createInstance() as Container<GenericContainer<*>>
        }.getOrElse { e ->
            throw IllegalStateException("Cannot instantiate container factory: ${factoryHint.qualifiedName}", e)
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
        return runCatching {
            customizerClass.createInstance() as ContainerCustomizer<T>
        }.getOrElse { e ->
            throw IllegalStateException("Cannot instantiate customizer: ${customizerClass.qualifiedName}", e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> resolveInjectable(injectableClass: KClass<out ContainerPropertyInjector<*>>): ContainerPropertyInjector<T> {
        return runCatching {
            injectableClass.createInstance() as ContainerPropertyInjector<T>
        }.getOrElse { e ->
            throw IllegalStateException("Cannot instantiate injectable: ${injectableClass.qualifiedName}", e)
        }
    }
}