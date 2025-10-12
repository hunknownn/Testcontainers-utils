package io.testcontainers.utils.core.listener

import io.testcontainers.utils.core.discovery.ContainerFactoryDiscovery
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

/**
 * Testconatiners-utils에서 제공하는 ContainerFactory를 자동으로 발견하고 등록하는 리스너
 */
class ContainerDiscovery : AbstractTestExecutionListener() {

    override fun getOrder() = HIGHEST_PRECEDENCE + 99

    override fun beforeTestClass(testContext: TestContext) {
        ContainerFactoryDiscovery.discoverAndRegisterFactories()
    }
}