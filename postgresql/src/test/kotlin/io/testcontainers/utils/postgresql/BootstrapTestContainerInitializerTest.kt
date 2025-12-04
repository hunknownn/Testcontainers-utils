package io.testcontainers.utils.postgresql

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Component
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [PostgresqlApplication::class])
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            component = Component.POSTGRESQL,
            image = "postgres:16",
            customizer = PostgresqlCustomizer::class
        )
    ]
)
class BootstrapTestContainerInitializerTest : StringSpec({

    "test initializer" {

    }

}) {
    override fun extensions() = listOf(SpringExtension)
}