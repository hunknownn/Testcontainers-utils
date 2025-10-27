package io.testcontainers.utils.core.listener

import com.github.dockerjava.api.command.CreateContainerCmd
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.core.ContainerPropertyResolver.resolveFactory
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import org.slf4j.LoggerFactory
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.testcontainers.UnstableAPI
import org.testcontainers.containers.GenericContainer
import org.testcontainers.shaded.com.fasterxml.jackson.databind.MapperFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature
import org.testcontainers.shaded.com.github.dockerjava.core.DefaultDockerClientConfig
import org.testcontainers.shaded.com.google.common.hash.Hashing

class TestcontainersStarter : AbstractTestExecutionListener() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getOrder() = HIGHEST_PRECEDENCE + 100

    override fun beforeTestClass(testContext: TestContext) {
        logger.info("Bootstrapping testcontainers for ${testContext.testClass.name}")

        val testClass = testContext.testClass
        val bootstrapped = testClass.getAnnotation(BootstrapTestcontainers::class.java)

        bootstrapped.properties.forEach { property ->
            val (key, containerConfiguration) = resolveFactory(property)
            logger.info("Resolved container configuration for ${property.component} with image ${property.image} by $key")

            val container = containerConfiguration.container

            containerConfiguration.customizeContainer()

            val isNewlyStarting = container.start(property.recycle, key)
            if (isNewlyStarting) {
                val env = testContext.applicationContext.environment as ConfigurableEnvironment
                containerConfiguration.inject(env)
            }
        }
    }

    private fun GenericContainer<*>.start(recycle: Recycle, key: String): Boolean {
        val hash = hash(this.dockerClient.createContainerCmd(this.dockerImageName))

        return when (recycle) {
            Recycle.MERGE -> {
                val isNewly = ContainerRegistry.mergeStart(hash, key) != null
                if (isNewly) this.start()
                return isNewly
            }

            Recycle.NEW -> {
                ContainerRegistry.newlyStart(hash, key, this)
                this.start()
                return true
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