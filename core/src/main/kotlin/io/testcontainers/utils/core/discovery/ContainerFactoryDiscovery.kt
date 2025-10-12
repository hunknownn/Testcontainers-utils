package io.testcontainers.utils.core.discovery

import io.testcontainers.utils.core.core.Container
import io.testcontainers.utils.core.core.ContainerRegistry
import org.slf4j.LoggerFactory
import org.springframework.core.io.support.SpringFactoriesLoader
import org.testcontainers.containers.GenericContainer

/**
 * 클래스패스에서 Container 구현체들을 자동으로 발견하고 등록하는 유틸리티
 *
 * Testcontainers-utils에서 제공하는 Container 구현체들을 등록한다
 */
object ContainerFactoryDiscovery {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * META-INF/spring.factories에서 Container 구현체들을 찾아 자동 등록
     */
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
                    val containerFactory = factory as Container<GenericContainer<*>>
                    ContainerRegistry.register(containerFactory.component, containerFactory)
                    logger.info("Registered container factory for component: ${containerFactory.component}")
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
