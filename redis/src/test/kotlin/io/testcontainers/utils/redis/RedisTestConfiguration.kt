package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class RedisTestConfiguration {

    @Bean
    fun redisContainer(): RedisContainer {
        val container = RedisContainer(DockerImageName.parse("redis:7.4"))
        container.start()
        return container
    }
}
