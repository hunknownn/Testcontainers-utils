package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.annotation.BootstrapTestContainer
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Component
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [PostgresqlApplication::class])
@BootstrapTestContainer(
    properties = [
        ContainerProperty(
            component = Component.POSTGRESQL,
            image = "postgres:16",
            customizer = PostgresCustomizer::class
        )
    ]
)
class BootstrapTestContainerInitializerTest3 {
    @Test
    fun test1() {

    }
}