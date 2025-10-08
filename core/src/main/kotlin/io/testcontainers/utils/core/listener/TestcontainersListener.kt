package io.testcontainers.utils.core.listener

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
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
    private fun resolveCustomizer(customizerClass: KClass<out ContainerCustomizer<*>>): ContainerCustomizer<GenericContainer<*>> {
        return customizerClass.createInstance() as ContainerCustomizer<GenericContainer<*>>
    }

    private fun getOrAdd(property: ContainerProperty): Container<*> {
        val factoryHint = property.factory
        if (factoryHint == Nothing::class) {
            // 팩토리가 등록되지 않은 경우 기본 팩토리를 동적으로 로드
            return try {
                ContainerRegistry.getFactory(property.component)
            } catch (e: IllegalStateException) {
                logger.warn("Factory for ${property.component} not found, attempting to load default factory")
                loadDefaultFactory(property.component)
            }
        }
        return ContainerRegistry.getFactoryOrAdd(property.component, property.factory.createInstance())
    }

    private fun loadDefaultFactory(component: io.testcontainers.utils.core.core.Component): Container<*> {
        return when (component) {
            io.testcontainers.utils.core.core.Component.POSTGRESQL -> {
                try {
                    // PostgreSQL 팩토리를 동적으로 로드
                    val factoryClass = Class.forName("io.testcontainers.utils.postgresql.PostgresContainerFactory")
                    val factory = factoryClass.getDeclaredConstructor().newInstance() as Container<*>
                    ContainerRegistry.register(component, factory)
                    logger.info("Dynamically loaded and registered factory for $component")
                    factory
                } catch (e: Exception) {
                    logger.error("Failed to load default factory for $component", e)
                    throw IllegalStateException("No factory available for $component", e)
                }
            }

            else -> throw IllegalStateException("No default factory available for $component")
        }
    }
}