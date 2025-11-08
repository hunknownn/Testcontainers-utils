package io.testcontainers.utils.core.context

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.bootstrap.TestcontainersBootstrapper
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration

class TestcontainersContextCustomizer(
    private val annotation: BootstrapTestcontainers
) : ContextCustomizer {

    override fun customizeContext(
        context: ConfigurableApplicationContext,
        mergedConfig: MergedContextConfiguration
    ) {
        TestcontainersBootstrapper.bootstrap(annotation, context.environment)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestcontainersContextCustomizer) return false
        return annotation == other.annotation
    }

    override fun hashCode(): Int {
        return annotation.hashCode()
    }
}
