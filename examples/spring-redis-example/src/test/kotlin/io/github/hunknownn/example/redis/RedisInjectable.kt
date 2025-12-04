package io.github.hunknownn.example.redis

import com.redis.testcontainers.RedisContainer
import io.testcontainers.utils.core.injectable.AbstractContainerPropertyInjector
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

class RedisInjectable : AbstractContainerPropertyInjector<RedisContainer>() {

    override val name = "example-redis"

    override fun inject(container: RedisContainer, environment: ConfigurableEnvironment) {
        val properties = mutableMapOf<String, Any>()

        properties["spring.data.redis.host"] = container.host
        properties["spring.data.redis.port"] = container.firstMappedPort
        properties["redis.uri"] = container.redisURI

        val propertySource = MapPropertySource(name, properties)

        inject(environment, propertySource)
    }
}
