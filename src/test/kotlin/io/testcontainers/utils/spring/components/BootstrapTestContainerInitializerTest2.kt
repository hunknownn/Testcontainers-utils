package io.testcontainers.utils.spring.components

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.testcontainers.utils.spring.TestApplication
import io.testcontainers.utils.annotation.BootstrapTestContainer
import io.testcontainers.utils.annotation.ContainerProperty
import io.testcontainers.utils.core.Component
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestApplication::class])
@BootstrapTestContainer(
    properties = [
        ContainerProperty(component = Component.POSTGRESQL)
    ]
)
class BootstrapTestContainerInitializerTest2 : StringSpec({

    "test initializer" {

    }

}) {
    override fun extensions() = listOf(SpringExtension)
}