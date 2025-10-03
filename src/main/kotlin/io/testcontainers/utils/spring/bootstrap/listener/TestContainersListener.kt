package io.testcontainers.utils.spring.bootstrap.listener

import io.testcontainers.utils.spring.bootstrap.annotations.BootstrapTestContainer
import io.testcontainers.utils.spring.bootstrap.annotations.ContainerProperty
import io.testcontainers.utils.spring.bootstrap.components.Component
import io.testcontainers.utils.spring.bootstrap.components.Container
import io.testcontainers.utils.spring.bootstrap.components.ContainerHouse
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import kotlin.reflect.full.createInstance

class TestContainersListener : AbstractTestExecutionListener() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("start!")
        val testClass = testContext.testClass
        val bootstrapped = testClass.getAnnotation(BootstrapTestContainer::class.java)

        bootstrapped.properties.forEach { property ->
            val factory = getOrAdd(property)
            val container = factory.container(property.image, property.reuse)
            logger.info("Container info: $container")
            container.start()
        }
    }

    private fun getOrAdd(property: ContainerProperty): Container<*> {
        val factoryHint = property.factory
        if (factoryHint == Nothing::class) return ContainerHouse.getFactory(property.component)
        return ContainerHouse.getFactoryOrAdd(property.component, property.factory.createInstance())
    }
}