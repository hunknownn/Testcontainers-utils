package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.testcontainers.postgresql.PostgreSQLContainer

class PostgresqlCustomizer : ContainerCustomizer<PostgreSQLContainer> {

    override fun customize(container: PostgreSQLContainer) {
        container.apply {
            withLabel("customizer", "PostgresqlCustomizer")
        }
    }
}