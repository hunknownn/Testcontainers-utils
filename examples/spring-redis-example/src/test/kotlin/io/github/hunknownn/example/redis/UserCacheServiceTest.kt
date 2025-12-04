package io.github.hunknownn.example.redis

import io.github.hunknownn.example.UserCacheService
import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.redis.RedisContainerFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            recycle = Recycle.NEW,
            factoryHint = RedisContainerFactory::class,
            image = "redis:7.4",
            customizer = RedisCustomizer::class,
            injectable = RedisInjectable::class
        )
    ]
)
class UserCacheServiceTest {

    @Autowired
    private lateinit var userCacheService: UserCacheService

    @AfterEach
    fun cleanup() {
        userCacheService.clearAll()
    }

    @Test
    fun `사용자를 캐시에 저장하고 조회할 수 있다`() {
        // given
        val userId = 1L
        val userName = "홍길동"

        // when
        userCacheService.cacheUser(userId, userName)

        // then
        val cachedUser = userCacheService.getCachedUser(userId)
        assertNotNull(cachedUser)
        assertEquals(userName, cachedUser)
    }

    @Test
    fun `캐시에 없는 사용자를 조회하면 null을 반환한다`() {
        // given
        val userId = 999L

        // when
        val cachedUser = userCacheService.getCachedUser(userId)

        // then
        assertNull(cachedUser)
    }

    @Test
    fun `캐시된 사용자를 삭제할 수 있다`() {
        // given
        val userId = 2L
        val userName = "김철수"
        userCacheService.cacheUser(userId, userName)

        // when
        userCacheService.evictUser(userId)

        // then
        val cachedUser = userCacheService.getCachedUser(userId)
        assertNull(cachedUser)
    }

    @Test
    fun `여러 사용자를 캐시에 저장하고 조회할 수 있다`() {
        // given
        val users = mapOf(
            1L to "사용자1",
            2L to "사용자2",
            3L to "사용자3"
        )

        // when
        users.forEach { (id, name) ->
            userCacheService.cacheUser(id, name)
        }

        // then
        users.forEach { (id, name) ->
            val cachedUser = userCacheService.getCachedUser(id)
            assertNotNull(cachedUser)
            assertEquals(name, cachedUser)
        }
    }

    @Test
    fun `캐시를 모두 삭제할 수 있다`() {
        // given
        userCacheService.cacheUser(1L, "사용자1")
        userCacheService.cacheUser(2L, "사용자2")
        userCacheService.cacheUser(3L, "사용자3")

        // when
        userCacheService.clearAll()

        // then
        assertNull(userCacheService.getCachedUser(1L))
        assertNull(userCacheService.getCachedUser(2L))
        assertNull(userCacheService.getCachedUser(3L))
    }

    @Test
    fun `같은 ID로 캐시를 덮어쓸 수 있다`() {
        // given
        val userId = 4L
        userCacheService.cacheUser(userId, "이전이름")

        // when
        userCacheService.cacheUser(userId, "새이름")

        // then
        val cachedUser = userCacheService.getCachedUser(userId)
        assertEquals("새이름", cachedUser)
    }
}
