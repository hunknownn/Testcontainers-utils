package io.testcontainers.utils.factory

import io.testcontainers.utils.core.Component
import io.testcontainers.utils.core.Container
import io.testcontainers.utils.core.Recycle
import io.testcontainers.utils.customizer.ContainerCustomizer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerFactory : Container<PostgreSQLContainer<*>> {

    override val component: Component = Component.POSTGRESQL

    companion object {
        @Volatile
        private var singletonInstance: PostgreSQLContainer<*>? = null

        private fun createContainer(image: String, customizer: ContainerCustomizer<PostgreSQLContainer<*>>): PostgreSQLContainer<*> {
            val dockerImageName = image.takeIf { it.isNotBlank() } ?: Component.POSTGRESQL.defaultImage
            return PostgreSQLContainer(DockerImageName.parse(dockerImageName)).apply {
                customizer.customize(this)
            }
        }

        fun getSingleton(image: String, customizer: ContainerCustomizer<PostgreSQLContainer<*>>): PostgreSQLContainer<*> {
            return singletonInstance ?: synchronized(this) {
                singletonInstance ?: createContainer(image, customizer).also { singletonInstance = it }
            }
        }
    }

    override fun recycle() = Recycle.MERGE

    override fun supports() = component

    @Suppress("UNCHECKED_CAST")
    override fun container(image: String, customizer: ContainerCustomizer<GenericContainer<*>>): PostgreSQLContainer<*> {
        val postgresCustomizer = customizer as ContainerCustomizer<PostgreSQLContainer<*>>
        return when (recycle()) {
            Recycle.MERGE -> getSingleton(image, postgresCustomizer)   // 싱글톤 재사용 (customizer 적용)
            Recycle.NEW -> createContainer(image, postgresCustomizer)  // 항상 새로운 컨테이너 생성 (customizer 적용)
        }
    }
}