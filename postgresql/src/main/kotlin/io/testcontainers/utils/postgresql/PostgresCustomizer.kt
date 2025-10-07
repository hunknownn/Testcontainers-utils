package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.testcontainers.containers.PostgreSQLContainer

class PostgresCustomizer: ContainerCustomizer<PostgreSQLContainer<*>> {
    override fun customize(container: PostgreSQLContainer<*>) {
        container.apply {
            withDatabaseName("qwer")
        }
    }
}
