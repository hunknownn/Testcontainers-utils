package io.testcontainers.utils.redis

import com.redis.testcontainers.RedisContainer
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.env.MockEnvironment
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(classes = [RedisApplication::class])
@ContextConfiguration(classes = [RedisTestConfiguration::class])
class RedisModuleIntegrationTest : DescribeSpec() {

    @Autowired
    private lateinit var redisContainer: RedisContainer

    override fun extensions() = listOf(SpringExtension)

    init {
        describe("Redis Module Integration with Spring Boot") {
            context("RedisContainerFactory + RedisCustomizer + RedisInjectable") {
                it("should create and customize container, then inject properties") {
                    val factory = RedisContainerFactory()
                    val customizer = RedisCustomizer()
                    val injectable = RedisInjectable()

                    // 1. Factory creates container
                    val container = factory.container()
                    container shouldNotBe null

                    // 2. Customizer adds label
                    customizer.customize(container)
                    container.labels["customizer"] shouldBe "RedisCustomizer"

                    // 3. Start container
                    container.start()

                    try {
                        container.isRunning shouldBe true

                        // 4. Injectable injects properties
                        val environment = MockEnvironment()
                        injectable.inject(container, environment)

                        val host = environment.getProperty("redis.host")
                        val port = environment.getProperty("redis.port", Int::class.java)
                        val uri = environment.getProperty("redis.uri")

                        host shouldBe "localhost"
                        port shouldBe container.firstMappedPort
                        uri shouldStartWith "redis://"

                        println("✓ Full integration successful")
                        println("  - Container created with image: ${container.dockerImageName}")
                        println("  - Customizer label: ${container.labels["customizer"]}")
                        println("  - Injected host: $host")
                        println("  - Injected port: $port")
                        println("  - Injected URI: $uri")
                    } finally {
                        container.stop()
                    }
                }

                it("should work with containerShell method") {
                    val factory = RedisContainerFactory()
                    val customizer = RedisCustomizer()
                    val injectable = RedisInjectable()

                    val config = factory.containerShell(
                        image = "redis:7.4",
                        recycle = io.testcontainers.utils.core.core.Recycle.MERGE,
                        customizer = customizer,
                        injectable = injectable
                    )

                    config shouldNotBe null
                    config.container shouldNotBe null

                    // Apply customization
                    config.customizeContainer()
                    config.container.labels["customizer"] shouldBe "RedisCustomizer"

                    println("✓ ContainerShell integration successful")
                    println("  - Configuration component: ${config.component}")
                    println("  - Configuration recycle: ${config.recycle}")
                }
            }

            context("Spring Boot managed container") {
                it("should have container bean available") {
                    redisContainer shouldNotBe null
                    redisContainer.isRunning shouldBe true

                    println("✓ Spring-managed container running")
                    println("  - Container URI: ${redisContainer.redisURI}")
                }
            }
        }
    }
}
