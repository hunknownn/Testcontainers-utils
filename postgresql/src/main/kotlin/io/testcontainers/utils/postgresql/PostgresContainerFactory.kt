package io.testcontainers.utils.postgresql

import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.Container
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerFactory : Container<PostgreSQLContainer<*>> {

    override val component: Component = Component.POSTGRESQL

    companion object {
        @Volatile
        private var singletonInstance: PostgreSQLContainer<*>? = null

        private fun createContainer(image: String): PostgreSQLContainer<*> {
            val dockerImageName = image.takeIf { it.isNotBlank() } ?: Component.POSTGRESQL.defaultImage
            return PostgreSQLContainer(DockerImageName.parse(dockerImageName))
        }

        fun getSingleton(image: String): PostgreSQLContainer<*> {
            return singletonInstance ?: synchronized(this) {
                singletonInstance ?: createContainer(image).also { singletonInstance = it }
            }
        }
    }

    override fun recycle() = Recycle.MERGE

    override fun supports() = component

    override fun customize(container: PostgreSQLContainer<*>) {
        container.apply {
            withDatabaseName("test")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun container(image: String, customizer: ContainerCustomizer<PostgreSQLContainer<*>>): PostgreSQLContainer<*> {
        val container =  when (recycle()) {
            Recycle.MERGE -> getSingleton(image)
            Recycle.NEW -> createContainer(image)
        }
        customize(container)
        customizer.customize(container)

        return container
    }
}