package io.testcontainers.utils.spring.components

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.testcontainers.utils.spring.TestApplication
import io.testcontainers.utils.spring.bootstrap.annotations.BootstrapTestContainer
import io.testcontainers.utils.spring.bootstrap.annotations.ContainerProperty
import io.testcontainers.utils.spring.bootstrap.components.Component
import io.testcontainers.utils.spring.bootstrap.components.PostgresFactory
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestApplication::class])
@BootstrapTestContainer(
    factories = [PostgresFactory::class],
    properties = [
        ContainerProperty(component = Component.POSTGRESQL, image = "postgres:16", reuse = false)
    ]
)
class BootstrapTestContainerInitializerTest : StringSpec({

    "test initializer" {

    }

}) {
    override fun extensions() = listOf(SpringExtension)
}