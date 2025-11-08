package io.testcontainers.utils.core.listener

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.bootstrap.TestcontainersBootstrapper
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

@Deprecated(
    message = "Use ContextCustomizer approach instead. This will be removed in a future version.",
    replaceWith = ReplaceWith("TestcontainersContextCustomizer")
)
class TestcontainersStarter : AbstractTestExecutionListener() {

    override fun getOrder() = HIGHEST_PRECEDENCE + 100

    override fun beforeTestClass(testContext: TestContext) {
        val testClass = testContext.testClass
        val annotation = testClass.getAnnotation(BootstrapTestcontainers::class.java) ?: return

        val env = testContext.applicationContext.environment as ConfigurableEnvironment
        TestcontainersBootstrapper.bootstrap(annotation, env)
    }
}