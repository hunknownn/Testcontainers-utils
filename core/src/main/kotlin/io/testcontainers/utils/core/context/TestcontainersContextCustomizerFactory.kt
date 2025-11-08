package io.testcontainers.utils.core.context

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import org.springframework.test.context.ContextConfigurationAttributes
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.ContextCustomizerFactory

class TestcontainersContextCustomizerFactory : ContextCustomizerFactory {

    override fun createContextCustomizer(
        testClass: Class<*>,
        configAttributes: MutableList<ContextConfigurationAttributes>
    ): ContextCustomizer? {
        val annotation = testClass.getAnnotation(BootstrapTestcontainers::class.java)
        return if (annotation != null) {
            TestcontainersContextCustomizer(annotation)
        } else {
            null
        }
    }
}
