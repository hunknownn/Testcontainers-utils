package io.testcontainers.utils.core.listener

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.Container
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.testcontainers.containers.GenericContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class TestcontainersListener : AbstractTestExecutionListener() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("start!")
        val testClass = testContext.testClass
        val bootstrapped = testClass.getAnnotation(BootstrapTestcontainers::class.java)

        bootstrapped.properties.forEach { property ->
            val factory = getOrAdd(property)
            val customizerInstance = resolveCustomizer(property.customizer)

            val container = factory.container(property.image, customizerInstance)
            logger.info("Container info: $container")
            container.start()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : GenericContainer<*>> resolveCustomizer(customizerClass: KClass<out ContainerCustomizer<*>>): ContainerCustomizer<T> {
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