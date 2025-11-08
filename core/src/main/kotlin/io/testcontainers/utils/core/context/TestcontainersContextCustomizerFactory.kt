package io.testcontainers.utils.core.context

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import org.slf4j.LoggerFactory
import org.springframework.test.context.ContextConfigurationAttributes
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.ContextCustomizerFactory

class TestcontainersContextCustomizerFactory : ContextCustomizerFactory {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createContextCustomizer(
        testClass: Class<*>,
        configAttributes: MutableList<ContextConfigurationAttributes>
    ): ContextCustomizer? {
        val annotation = testClass.getAnnotation(BootstrapTestcontainers::class.java)
        return if (annotation != null) {
            logger.debug("Creating TestcontainersContextCustomizer for ${testClass.simpleName}")
            TestcontainersContextCustomizer(annotation)
        } else {
            null
        }
    }
}
