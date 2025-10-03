package io.testcontainers.utils.spring.bootstrap.components

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerFactory : Container<PostgreSQLContainer<*>> {

    override val component: Component = Component.POSTGRESQL

    companion object {
        @Volatile
        private var singletonInstance: PostgreSQLContainer<*>? = null

        private fun createContainer(image: String, reuse: Boolean): PostgreSQLContainer<*> {
            val dockerImageName = image.takeIf { it.isNotBlank() } ?: Component.POSTGRESQL.defaultImage
            return PostgreSQLContainer(DockerImageName.parse(dockerImageName))
                .withReuse(reuse)
                .apply { start() }
        }

        fun getSingleton(image: String, reuse: Boolean): PostgreSQLContainer<*> {
            return singletonInstance ?: synchronized(this) {
                singletonInstance ?: createContainer(image, reuse).also { singletonInstance = it }
            }
        }
    }

    override fun recycle() = Recycle.MERGE

    override fun supports() = component

    override fun container(image: String, reuse: Boolean): PostgreSQLContainer<*> {
        return when (recycle()) {
            Recycle.MERGE -> getSingleton(image, reuse)   // 싱글톤 재사용
            Recycle.NEW -> createContainer(image, reuse)  // 항상 새로운 컨테이너 생성
        }
    }
}