package io.testcontainers.utils.core.customizer

import org.testcontainers.containers.PostgreSQLContainer

class PostgresCustomizer: ContainerCustomizer<PostgreSQLContainer<*>> {
    override fun customize(container: PostgreSQLContainer<*>) {
        container.apply {
            withDatabaseName("qwer")
        }
    }
}
