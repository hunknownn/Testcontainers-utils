package io.testcontainers.utils.spring.bootstrap.components

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainerFactory : Container<PostgreSQLContainer<*>> {
    private val supportComponent = Component.POSTGRESQL
    override val component: Component = Component.POSTGRESQL

    override fun recycle() = Recycle.MERGE

    override fun supports() = component

    override fun container(image: String, reuse: Boolean): PostgreSQLContainer<*> {
        val dockerImageName =
            image.takeIf { it.isNotBlank() } ?: supportComponent.defaultImage

        return PostgreSQLContainer(DockerImageName.parse(dockerImageName))
            .withReuse(reuse)
        // 몇가지 기본 설정 전략 추가
    }
}