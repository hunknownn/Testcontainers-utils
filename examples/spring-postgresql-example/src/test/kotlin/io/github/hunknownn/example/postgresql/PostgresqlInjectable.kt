package io.github.hunknownn.example.postgresql

import io.testcontainers.utils.core.injectable.AbstractContainerPropertyInjector
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.testcontainers.postgresql.PostgreSQLContainer

class PostgresqlInjectable : AbstractContainerPropertyInjector<PostgreSQLContainer>() {

    override val name = "example-postgresql"

    override fun inject(container: PostgreSQLContainer, environment: ConfigurableEnvironment) {

        val properties = mutableMapOf<String, Any>()
        properties["spring.jpa.hibernate.ddl-auto"] = "update"
        properties["spring.datasource.url"] = container.jdbcUrl
        properties["spring.datasource.username"] = container.username
        properties["spring.datasource.password"] = container.password
        properties["spring.datasource.driver-class-name"] = container.driverClassName

        val propertySource = MapPropertySource(name, properties)

        inject(environment, propertySource)
    }
}