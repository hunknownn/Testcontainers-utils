package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.core.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerFactory : AbstractContainer<PostgreSQLContainer<*>>() {

    override val component: Component = Component.POSTGRESQL

    override fun supports() = component

    override fun container(): PostgreSQLContainer<*> {
        return container(component.defaultImage)
    }

    @Suppress("UNCHECKED_CAST")
    override fun container(image: String): PostgreSQLContainer<*> {
        val dockerImageName = image.takeIf { it.isNotBlank() } ?: Component.POSTGRESQL.defaultImage
        return PostgreSQLContainer(DockerImageName.parse(dockerImageName))
    }

    override fun customize(container: PostgreSQLContainer<*>) {
        container.apply {
            withDatabaseName("test")
        }
    }
}