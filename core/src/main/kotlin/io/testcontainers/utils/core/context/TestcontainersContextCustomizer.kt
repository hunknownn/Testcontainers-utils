package io.testcontainers.utils.core.context

import com.github.dockerjava.api.command.CreateContainerCmd
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.core.ContainerPropertyResolver.resolveFactory
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import org.slf4j.LoggerFactory
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import org.testcontainers.UnstableAPI
import org.testcontainers.containers.GenericContainer
import org.testcontainers.shaded.com.fasterxml.jackson.databind.MapperFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature
import org.testcontainers.shaded.com.github.dockerjava.core.DefaultDockerClientConfig
import org.testcontainers.shaded.com.google.common.hash.Hashing

class TestcontainersContextCustomizer(
    private val annotation: BootstrapTestcontainers
) : ContextCustomizer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun customizeContext(
        context: ConfigurableApplicationContext,
        mergedConfig: MergedContextConfiguration
    ) {
        logger.info("Customizing context for testcontainers")

        annotation.properties.forEach { property ->
            val (key, containerConfiguration) = resolveFactory(property)
            logger.info("Resolved container configuration for ${property.component} with image ${property.image} by $key")

            val container = containerConfiguration.container

            containerConfiguration.customizeContainer()

            val isNewlyStarting = container.start(property.recycle, key)
            if (isNewlyStarting) {
                val env = context.environment
                containerConfiguration.inject(env)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestcontainersContextCustomizer) return false
        return annotation == other.annotation
    }

    override fun hashCode(): Int {
        return annotation.hashCode()
    }

    private fun GenericContainer<*>.start(recycle: Recycle, key: String): Boolean {
        val hash = hash(this.dockerClient.createContainerCmd(this.dockerImageName))

        return when (recycle) {
            Recycle.MERGE -> {
                val isNewly = ContainerRegistry.mergeStart(hash, key) != null
                if (isNewly) this.start()
                isNewly
            }

            Recycle.NEW -> {
                ContainerRegistry.newlyStart(hash, key, this)
                this.start()
                true
            }
        }
    }

    @UnstableAPI
    fun hash(createCommand: CreateContainerCmd?): String {
        val dockerClientConfig: DefaultDockerClientConfig =
            DefaultDockerClientConfig.createDefaultConfigBuilder().build()

        val commandJson: ByteArray = dockerClientConfig
            .objectMapper
            .copy()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .writeValueAsBytes(createCommand)

        // TODO add Testcontainers' version to the hash
        return Hashing.sha256().hashBytes(commandJson).toString()
    }
}
