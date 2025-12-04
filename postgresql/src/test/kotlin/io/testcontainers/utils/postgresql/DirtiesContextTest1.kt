package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Component
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            component = Component.POSTGRESQL,
            image = "postgres:16",
            customizer = PostgresqlCustomizer::class
        )
    ]
)
class DirtiesContextTest1 {
    @Test
    fun testOne()  {
        println("DirtiesContextTest1.testOne")
    }

    @Test
    fun testTwo() {
        println("DirtiesContextTest1.testTwo")
    }
}