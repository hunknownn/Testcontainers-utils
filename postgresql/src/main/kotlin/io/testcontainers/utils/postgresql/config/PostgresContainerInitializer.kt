package io.testcontainers.utils.postgresql.config

import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.postgresql.PostgresContainerFactory
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class PostgresContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        logger.info("Registering PostgreSQL container factory")
        try {
            ContainerRegistry.register(Component.POSTGRESQL, PostgresContainerFactory())
            logger.info("PostgreSQL container factory registered successfully")
        } catch (e: Exception) {
            logger.error("Failed to register PostgreSQL container factory", e)
        }
    }
}
