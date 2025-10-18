package io.testcontainers.utils.core.listener

import com.github.dockerjava.api.command.CreateContainerCmd
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.core.ContainerPropertyResolver.resolveFactory
import org.slf4j.LoggerFactory
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.testcontainers.UnstableAPI
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
            val containerConfiguration = resolveFactory(property)
            logger.info("Resolved container configuration for ${property.component} with image ${property.image} by ${containerConfiguration.key()}")
            val container = containerConfiguration.container
            container.apply {
                withStartupAttempts(3)
                getCreateContainerCmdModifiers()
            }
            val containerCmd1 = container.dockerClient.createContainerCmd(container.dockerImageName)

            containerConfiguration.customizeContainer()
            val containerCmd2 = container.dockerClient.createContainerCmd(container.dockerImageName)
//            val hash = containerCmd
            println("containerCmd1 = ${containerCmd1}")
            println("containerCmd2 = ${containerCmd2}")
            container.start()

            val env = testContext.applicationContext.environment as ConfigurableEnvironment
            containerConfiguration.inject(env)
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