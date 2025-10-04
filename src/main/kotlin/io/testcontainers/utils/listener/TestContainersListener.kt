package io.testcontainers.utils.listener

import io.testcontainers.utils.annotation.BootstrapTestContainer
import io.testcontainers.utils.annotation.ContainerProperty
import io.testcontainers.utils.core.Container
import io.testcontainers.utils.core.ContainerRegistry
import io.testcontainers.utils.customizer.ContainerCustomizer
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.testcontainers.containers.GenericContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class TestContainersListener : AbstractTestExecutionListener() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("start!")
        val testClass = testContext.testClass
        val bootstrapped = testClass.getAnnotation(BootstrapTestContainer::class.java)

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
        if (factoryHint == Nothing::class) return ContainerRegistry.getFactory(property.component)
        return ContainerRegistry.getFactoryOrAdd(property.component, property.factory.createInstance())
    }
}