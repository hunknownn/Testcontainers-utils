package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import io.testcontainers.utils.core.core.AbstractContainer
import io.testcontainers.utils.core.core.Component
import org.testcontainers.utility.DockerImageName

class RedisContainerFactory : AbstractContainer<RedisContainer>() {

    override val component: Component = Component.REDIS

    override fun supports() = component

    override fun container(): RedisContainer {
        return container(component.defaultImage)
    }

    override fun container(image: String): RedisContainer {
        val dockerImageName = image.takeIf { it.isNotBlank() } ?: component.defaultImage
        return RedisContainer(DockerImageName.parse(dockerImageName))
    }

    override fun customize(container: RedisContainer) {}
}