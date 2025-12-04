package io.testcontainers.utils.redis

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.Recycle

class RedisContainerFactoryTest : DescribeSpec({

    describe("RedisContainerFactory") {
        val factory = RedisContainerFactory()

        context("component") {
            it("should return REDIS component") {
                factory.component shouldBe Component.REDIS
            }

            it("should support REDIS component") {
                factory.supports() shouldBe Component.REDIS
            }
        }

        context("container creation") {
            it("should create container with default image") {
                val container = factory.container()

                container shouldNotBe null
                container.dockerImageName shouldContain "redis"
                container.dockerImageName shouldContain "7.4"

                println("Default container created: ${container.dockerImageName}")
            }

            it("should create container with custom image") {
                val customImage = "redis:7.2-alpine"
                val container = factory.container(customImage)

                container shouldNotBe null
                container.dockerImageName shouldContain "redis:7.2-alpine"

                println("Custom container created: ${container.dockerImageName}")
            }

            it("should create container with empty string (fallback to default)") {
                val container = factory.container("")

                container shouldNotBe null
                container.dockerImageName shouldContain "redis"
                container.dockerImageName shouldContain "7.4"

                println("Empty string fallback to default: ${container.dockerImageName}")
            }
        }

        context("containerShell") {
            it("should create configuration with default settings") {
                val config = factory.containerShell(
                    recycle = Recycle.MERGE,
                    customizer = null,
                    injectable = null
                )

                config shouldNotBe null
                config.component shouldBe Component.REDIS
                config.recycle shouldBe Recycle.MERGE
                config.container shouldNotBe null

                println("Configuration created with default settings")
            }

            it("should create configuration with custom image") {
                val customImage = "redis:7.0"
                val config = factory.containerShell(
                    image = customImage,
                    recycle = Recycle.MERGE,
                    customizer = null,
                    injectable = null
                )

                config shouldNotBe null
                config.component shouldBe Component.REDIS
                config.container.dockerImageName shouldContain "redis:7.0"

                println("Configuration created with custom image: ${config.container.dockerImageName}")
            }

            it("should create configuration with customizer") {
                val customizer = RedisCustomizer()
                val config = factory.containerShell(
                    recycle = Recycle.MERGE,
                    customizer = customizer,
                    injectable = null
                )

                config shouldNotBe null
                config.container shouldNotBe null

                // Customize가 호출되어야 함
                config.customizeContainer()
                config.container.labels["customizer"] shouldBe "RedisCustomizer"

                println("Configuration with customizer created and applied")
            }

            it("should create configuration with injectable") {
                val injectable = RedisInjectable()
                val config = factory.containerShell(
                    recycle = Recycle.MERGE,
                    customizer = null,
                    injectable = injectable
                )

                config shouldNotBe null
                config.container shouldNotBe null

                println("Configuration with injectable created")
            }
        }

        context("customize method") {
            it("should not throw exception when called") {
                val container = factory.container()

                // customize는 빈 구현이므로 예외가 발생하지 않아야 함
                factory.customize(container)

                println("Customize method executed without errors")
            }
        }
    }
})
