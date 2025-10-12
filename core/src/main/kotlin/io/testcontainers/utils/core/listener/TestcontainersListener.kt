package io.testcontainers.utils.core.listener

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.core.ContainerPropertyResolver.resolveCustomizer
import io.testcontainers.utils.core.core.ContainerPropertyResolver.resolveFactory
import org.slf4j.LoggerFactory
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class TestcontainersListener : AbstractTestExecutionListener() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("Bootstrapping testcontainers for ${testContext.testClass.name}")
        val testClass = testContext.testClass
        val bootstrapped = testClass.getAnnotation(BootstrapTestcontainers::class.java)

        bootstrapped.properties.forEach { property ->
            val factory = resolveFactory(property)
            val customizerInstance = resolveCustomizer(property.customizer)
            val container = factory.container(property.image, customizerInstance)
            logger.info("Starting container for ${property.component} with image ${property.image}")
            container.start()

            // 팩토리에 환경변수 주입 위임
            val env = testContext.applicationContext.environment as ConfigurableEnvironment
            factory.injectProperties(container, env)
        }
    }
}