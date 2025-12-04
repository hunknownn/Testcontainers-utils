package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.testcontainers.utility.DockerImageName

class RedisCustomizerTest : DescribeSpec({

    describe("RedisCustomizer") {
        val customizer = RedisCustomizer()

        context("customize method") {
            it("should add customizer label to container") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))

                customizer.customize(container)

                container.labels["customizer"] shouldBe "RedisCustomizer"

                println("Customizer label added: ${container.labels["customizer"]}")
            }

            it("should be idempotent - multiple calls should not cause issues") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))

                customizer.customize(container)
                customizer.customize(container)
                customizer.customize(container)

                container.labels["customizer"] shouldBe "RedisCustomizer"

                println("Multiple customize calls handled correctly")
            }

            it("should not affect other container properties") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))
                val originalImage = container.dockerImageName

                customizer.customize(container)

                container.dockerImageName shouldBe originalImage
                println("Container image unchanged: ${container.dockerImageName}")
            }

            it("should work with different Redis images") {
                val images = listOf("redis:7.4", "redis:7.2", "redis:7.0")

                images.forEach { image ->
                    val container = RedisContainer(DockerImageName.parse(image))
                    customizer.customize(container)

                    container.labels["customizer"] shouldBe "RedisCustomizer"
                    println("Customizer works with image: $image")
                }
            }
        }

        context("integration with container lifecycle") {
            it("should not prevent container from starting") {
                val container = RedisContainer(DockerImageName.parse("redis:7.4"))

                customizer.customize(container)
                container.start()

                container.isRunning shouldBe true
                container.labels["customizer"] shouldBe "RedisCustomizer"
                container.redisURI shouldNotBe null

                println("Container started successfully with customizer")
                println("Redis URI: ${container.redisURI}")

                container.stop()
            }
        }
    }
})
