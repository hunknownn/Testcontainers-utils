package io.testcontainers.utils.core

import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.container.TestGenericContainer
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.core.customizer.TestAlpineCustomizer
import io.testcontainers.utils.core.injectable.MasterInjectable
import io.testcontainers.utils.core.injectable.SlaveInjectable
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            value = "master",
            recycle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.17",
            customizer = TestAlpineCustomizer::class,
            injectable = MasterInjectable::class,
        ),
        ContainerProperty(
            value = "slave",
            recycle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.18",
            customizer = TestAlpineCustomizer::class,
            injectable = SlaveInjectable::class,
        ),
        ContainerProperty(
            value = "temporary",
            recycle = Recycle.NEW,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.18",
            customizer = TestAlpineCustomizer::class,
            injectable = MasterInjectable::class,
        ),
        ContainerProperty(
            value = "generic4",
            recycle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.18",
            customizer = TestAlpineCustomizer::class,
            injectable = MasterInjectable::class,
        )
    ]
)
class ContainerCoreTest {

    @Test
    fun `test container isRunning`() {
        val configurations = ContainerRegistry.getConfigurations()
        configurations.size shouldBeGreaterThanOrEqual 4
        configurations.filter { it.value.container.isRunning() }.size shouldBeGreaterThanOrEqual 3
    }

    @Test
    fun `test container image`() {
        val configurations = ContainerRegistry.getConfigurations()

        configurations.size shouldBeGreaterThanOrEqual 2
        val configuredImages = configurations.map { it.key.split("#")[1] }

        val expectedImages = setOf("alpine:3.17", "alpine:3.18")
        expectedImages.forEach {name ->
            name shouldBeIn configuredImages
        }
    }
}