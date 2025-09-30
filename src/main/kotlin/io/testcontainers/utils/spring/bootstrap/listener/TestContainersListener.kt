package io.testcontainers.utils.spring.bootstrap.listener

import io.testcontainers.utils.spring.bootstrap.annotations.BootstrapTestContainer
import io.testcontainers.utils.spring.bootstrap.components.Component
import io.testcontainers.utils.spring.bootstrap.components.Container
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.full.createInstance

class TestContainersListener : AbstractTestExecutionListener() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val factories: ConcurrentHashMap<Component, Container<*>> = ConcurrentHashMap()

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("start!")
        val testClass = testContext.testClass
        val bootstrapped = testClass.getAnnotation(BootstrapTestContainer::class.java)

        bootstrapped.factories.forEach { f ->
            val factory = f.createInstance()
            factories[factory.component] = factory
        }

        val props = bootstrapped.properties
        props.forEach { property ->
            val factory = factories[property.component] ?: error("No factory registered ${property.component}")
            val container = factory.container(property.image, property.reuse)
            container.start()
        }
    }
}