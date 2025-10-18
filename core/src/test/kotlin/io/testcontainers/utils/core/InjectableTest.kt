package io.testcontainers.utils.core

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.ContainerRegistry
import io.testcontainers.utils.core.core.Recycle
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment

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
        )
    ]
)
class InjectableTest {

    @Test
    fun `Generic 컨테이너 환경 변수 주입 테스트`() {
        val environment = ApplicationContextProvider.getApplicationContext().environment as ConfigurableEnvironment
        val configuration = ContainerRegistry.getConfiguration("generic1")
        val container = configuration.container

        val imageProperty = environment.getProperty("testcontainers.generic.image")
        imageProperty.shouldNotBeNull()
        imageProperty.toString() shouldBeEqual container.dockerImageName

        val containerIdProperty = environment.getProperty("testcontainers.generic.container-id")
        containerIdProperty.shouldNotBeNull()
        containerIdProperty shouldBeEqual container.containerId
    }
}