package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.core.AbstractContainer
import io.testcontainers.utils.core.core.Component
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerFactory : AbstractContainer<PostgreSQLContainer>() {

    override val component: Component = Component.POSTGRESQL

    override fun supports() = component

    override fun container(): PostgreSQLContainer {
        return container(component.defaultImage)
    }

    override fun container(image: String): PostgreSQLContainer {
        val dockerImageName = image.takeIf { it.isNotBlank() } ?: component.defaultImage
        return PostgreSQLContainer(DockerImageName.parse(dockerImageName))
    }

    override fun customize(container: PostgreSQLContainer) {
        container.apply {
            withDatabaseName("test")
        }
    }
}