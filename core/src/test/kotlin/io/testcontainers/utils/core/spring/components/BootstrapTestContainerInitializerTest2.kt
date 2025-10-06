package io.testcontainers.utils.core.spring.components

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.testcontainers.utils.core.CoreApplicationTests
import io.testcontainers.utils.core.annotation.BootstrapTestContainer
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Component
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [CoreApplicationTests::class])
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