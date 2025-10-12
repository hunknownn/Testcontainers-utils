package io.testcontainers.utils.postgresql.config

import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.postgresql.PostgresContainerFactory
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class ContainerRegisterer : AbstractTestExecutionListener() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getOrder() = HIGHEST_PRECEDENCE + 50

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("Registering PostgreSQL container factory")
        try {
            ContainerRegistry.register(Component.POSTGRESQL, PostgresContainerFactory())
            logger.info("PostgreSQL container factory registered successfully")
        } catch (e: Exception) {
            logger.error("Failed to register PostgreSQL container factory", e)
        }
    }
}
