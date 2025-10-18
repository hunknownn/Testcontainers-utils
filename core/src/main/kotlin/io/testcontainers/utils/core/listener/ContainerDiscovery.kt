package io.testcontainers.utils.core.listener

import io.testcontainers.utils.core.discovery.ContainerFactoryDiscovery
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class ContainerDiscovery : AbstractTestExecutionListener() {

    override fun getOrder() = HIGHEST_PRECEDENCE + 99

    override fun beforeTestClass(testContext: TestContext) {
        ContainerFactoryDiscovery.discoverAndRegisterFactories()
    }
}