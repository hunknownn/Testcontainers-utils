package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import io.testcontainers.utils.core.injectable.AbstractContainerPropertyInjector
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

class RedisInjectable : AbstractContainerPropertyInjector<RedisContainer>() {

    override val name = "redis-testcontainers"

    override fun inject(container: RedisContainer, environment: ConfigurableEnvironment) {
        val properties = mutableMapOf<String, Any>()

        properties["redis.host"] = container.host
        properties["redis.port"] = container.firstMappedPort
        properties["redis.uri"] = container.redisURI

        val propertySource = MapPropertySource(name, properties)
        inject(environment, propertySource)
    }
}
