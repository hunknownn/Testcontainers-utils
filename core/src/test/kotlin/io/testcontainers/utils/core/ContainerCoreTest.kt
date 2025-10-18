package io.testcontainers.utils.core

import io.kotest.matchers.be
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.should
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            value = "generic1",
            recyle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.17",
            customizer = TestAlpineCustomizer::class,
            injectable = TestInjectable::class,
        ),
        ContainerProperty(
            value = "generic2",
            recyle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.17",
            customizer = TestAlpineCustomizer::class,
            injectable = TestInjectable::class,
        ),
        ContainerProperty(
            value = "generic3",
            recyle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.17",
            customizer = TestAlpineCustomizer::class,
            injectable = TestInjectable::class,
        )
    ]
)
class ContainerCoreTest {

    @Test
    fun `test container isRunning`() {
        val configurations = ContainerRegistry.getConfigurations()
        configurations.size shouldBeGreaterThan 0

        configurations.forEach { node ->
            val container = node.value.container
            container.isRunning should be(true)
        }
    }

    @Test
    fun `test container image`() {
        val configurations = ContainerRegistry.getConfigurations()
        println("configurations = ${configurations}")
        configurations.size shouldBeGreaterThan 0

        configurations.forEach { node ->
            val container = node.value.container
            container.dockerImageName should be("alpine:3.17")
        }
    }
}