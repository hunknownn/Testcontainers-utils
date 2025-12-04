package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.mock.env.MockEnvironment
import org.testcontainers.utility.DockerImageName

class RedisInjectableTest : DescribeSpec({

    describe("RedisInjectable") {
        val injectable = RedisInjectable()

        context("name property") {
            it("should have correct property source name") {
                injectable.name shouldBe "redis-testcontainers"
            }
        }

        context("inject method") {
            it("should inject Redis properties into environment") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))
                container.start()

                try {
                    val environment: ConfigurableEnvironment = MockEnvironment()

                    injectable.inject(container, environment)

                    val host = environment.getProperty("redis.host")
                    val port = environment.getProperty("redis.port")
                    val uri = environment.getProperty("redis.uri")

                    host shouldNotBe null
                    host shouldBe "localhost"

                    port shouldNotBe null
                    println("Injected redis.port: $port")

                    uri shouldNotBe null
                    uri shouldStartWith "redis://"
                    println("Injected redis.uri: $uri")
                } finally {
                    container.stop()
                }
            }

            it("should inject correct port from container") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))
                container.start()

                try {
                    val environment: ConfigurableEnvironment = MockEnvironment()

                    injectable.inject(container, environment)

                    val injectedPort = environment.getProperty("redis.port", Int::class.java)
                    val actualPort = container.firstMappedPort

                    injectedPort shouldBe actualPort
                    println("Port verification - Injected: $injectedPort, Actual: $actualPort")
                } finally {
                    container.stop()
                }
            }

            it("should inject URI matching container's redisURI") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))
                container.start()

                try {
                    val environment: ConfigurableEnvironment = MockEnvironment()

                    injectable.inject(container, environment)

                    val injectedUri = environment.getProperty("redis.uri")
                    val actualUri = container.redisURI

                    injectedUri shouldBe actualUri
                    println("URI verification - Injected: $injectedUri")
                } finally {
                    container.stop()
                }
            }

            it("should create property source with correct name") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))
                container.start()

                try {
                    val environment: ConfigurableEnvironment = MockEnvironment()

                    injectable.inject(container, environment)

                    val propertySource = environment.propertySources.get(injectable.name)
                    propertySource shouldNotBe null
                    propertySource?.name shouldBe "redis-testcontainers"

                    println("Property source created: ${propertySource?.name}")
                } finally {
                    container.stop()
                }
            }
        }

        context("multiple injections") {
            it("should handle multiple inject calls") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))
                container.start()

                try {
                    val environment: ConfigurableEnvironment = MockEnvironment()

                    injectable.inject(container, environment)
                    injectable.inject(container, environment)

                    val host = environment.getProperty("redis.host")
                    host shouldBe "localhost"

                    println("Multiple injections handled correctly")
                } finally {
                    container.stop()
                }
            }
        }
    }
})
