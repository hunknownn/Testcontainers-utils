package io.github.hunknownn.example.redis

import com.redis.testcontainers.RedisContainer
import io.testcontainers.utils.core.customizer.ContainerCustomizer

class RedisCustomizer : ContainerCustomizer<RedisContainer> {

    override fun customize(container: RedisContainer) {
        container.withLabel("example", "spring-redis-test")
    }
}
