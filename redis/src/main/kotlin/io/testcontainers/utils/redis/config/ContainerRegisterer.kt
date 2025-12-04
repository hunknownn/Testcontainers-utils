package io.testcontainers.utils.redis.config

import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.ContainerConfiguration
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.redis.RedisContainerFactory
import io.testcontainers.utils.redis.RedisCustomizer
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class ContainerRegisterer : AbstractTestExecutionListener() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getOrder() = HIGHEST_PRECEDENCE + 50

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("Registering Redis container factory")
        try {
            val config = ContainerConfiguration(
                Component.REDIS,
                Recycle.MERGE,
                RedisContainerFactory::class,
                RedisContainerFactory().container(),
                customize = { RedisCustomizer() },
                injectable = { _, _ -> },
            )
            ContainerRegistry.registerNewly(config.key(""), config)
            logger.info("Redis container factory registered successfully")
        } catch (e: Exception) {
            logger.error("Failed to register Redis container factory", e)
        }
    }
}
