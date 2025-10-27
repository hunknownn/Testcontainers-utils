package io.testcontainers.utils.core

import io.kotest.matchers.equals.shouldBeEqual
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.container.TestGenericContainer
import io.testcontainers.utils.core.core.*
import io.testcontainers.utils.core.customizer.TestAlpineCustomizer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.17",
            customizer = TestAlpineCustomizer::class
        ),
        ContainerProperty(
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.18",
            customizer = TestAlpineCustomizer::class
        ),
        ContainerProperty(
            factoryHint = TestGenericContainer::class,
            image = "alpine:latest",
            customizer = TestAlpineCustomizer::class
        )
    ]
)
class CustomizerTest {

    @Test
    fun `Customizer 기본 기능 테스트`() {
        val configurations = ContainerRegistry.getConfigurations()
        configurations.forEach { node ->
            val key = node.key
            if(!key.startsWith("TestGenericContainer")) return@forEach
            val container = node.value.container

            container.labels["factory"]!! shouldBeEqual "TestGenericContainer"
            container.labels["managed-by"]!! shouldBeEqual "testcontainers-utils"
            container.labels["test-type"]!! shouldBeEqual "customizer"
            container.labels["container-type"]!! shouldBeEqual "alpine"
            container.labels["purpose"]!! shouldBeEqual "generic-test"
            container.labels["version"]!! shouldBeEqual "1.0.0"
        }
    }
}