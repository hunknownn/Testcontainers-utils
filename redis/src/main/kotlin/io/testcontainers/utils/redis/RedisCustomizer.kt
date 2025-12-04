package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import io.testcontainers.utils.core.customizer.ContainerCustomizer

class RedisCustomizer : ContainerCustomizer<RedisContainer> {

    override fun customize(container: RedisContainer) {
        container.withLabel("customizer", "RedisCustomizer")
    }
}