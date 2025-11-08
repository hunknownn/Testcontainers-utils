package io.github.hunknownn.example

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.testcontainers.containers.PostgreSQLContainer

class ExampleCustomizer : ContainerCustomizer<PostgreSQLContainer<*>>{

    override fun customize(container: PostgreSQLContainer<*>) {
        container.apply {
            container.withUsername("test")
            container.withPassword("test")
        }
    }
}