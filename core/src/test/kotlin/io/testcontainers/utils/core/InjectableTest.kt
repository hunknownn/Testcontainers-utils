package io.testcontainers.utils.core

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.container.TestGenericContainer
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.core.customizer.TestAlpineCustomizer
import io.testcontainers.utils.core.injectable.MasterInjectable
import io.testcontainers.utils.core.injectable.SlaveInjectable
import io.testcontainers.utils.core.provider.ApplicationContextProvider
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment
import java.time.Instant

@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            value = "master",
            recycle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.15",
            customizer = TestAlpineCustomizer::class,
            injectable = MasterInjectable::class,
        ),
        ContainerProperty(
            value = "slave",
            recycle = Recycle.MERGE,
            factoryHint = TestGenericContainer::class,
            image = "alpine:3.16",
            customizer = TestAlpineCustomizer::class,
            injectable = SlaveInjectable::class,
        )
    ]
)
class InjectableTest {

    @Test
    fun `Master 컨테이너 환경 변수 주입 테스트`() {
        val environment = ApplicationContextProvider.getApplicationContext().environment as ConfigurableEnvironment
        val configuration = ContainerRegistry.getConfiguration("master#alpine:3.15")
        val container = configuration.container

        val imageProperty = environment.getProperty("master.generic.image")
        imageProperty.shouldNotBeNull()
        imageProperty.toString() shouldBeEqual container.dockerImageName

        val containerIdProperty = environment.getProperty("master.generic.container-id")
        containerIdProperty.shouldNotBeNull()
        containerIdProperty shouldBeEqual container.containerId
    }
}