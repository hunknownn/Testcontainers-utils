package io.testcontainers.utils.core.discovery

import io.testcontainers.utils.core.core.Container
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import org.slf4j.LoggerFactory
import org.springframework.core.io.support.SpringFactoriesLoader
import org.testcontainers.containers.GenericContainer

object ContainerFactoryDiscovery {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun discoverAndRegisterFactories() {
        try {
            logger.info("Starting container factory discovery...")

            val factories = SpringFactoriesLoader.loadFactories(
                Container::class.java,
                Thread.currentThread().contextClassLoader
            )

            factories.forEach { factory ->
                try {
                    @Suppress("UNCHECKED_CAST")
                    val factoryHint = factory as Container<GenericContainer<*>>
                    val containerConfiguration = factoryHint.containerShell(Recycle.NEW, null, null)
                    val key = containerConfiguration.key("")
                    ContainerRegistry.register(containerConfiguration.component, key, containerConfiguration)
                    logger.debug("Registered container factory for component: ${factoryHint.component}")
                } catch (e: Exception) {
                    logger.error("Failed to register factory: ${factory::class.java.name}", e)
                }
            }

            logger.info("Container factory discovery completed. Registered ${factories.size} factories.")

        } catch (e: Exception) {
            logger.error("Failed to discover container factories", e)
        }
    }
}
