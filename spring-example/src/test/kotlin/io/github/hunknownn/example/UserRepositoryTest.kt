package io.github.hunknownn.example

import io.testcontainers.utils.core.annotation.BootstrapTestcontainers
import io.testcontainers.utils.core.annotation.ContainerProperty
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.postgresql.PostgresContainerFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
//            recycle = Recycle.MERGE,
            recycle = Recycle.NEW,
            factoryHint = PostgresContainerFactory::class,
            image = "postgres:16",
            customizer = ExampleCustomizer::class,
            injectable = ExampleInjectable::class
        )
    ]
)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @AfterEach
    fun cleanup() {
        userRepository.deleteAll()
    }

    @Test
    fun `사용자를 저장하고 조회할 수 있다`() {
        // given
        val user = User(
            name = "홍길동",
            email = "hong@example.com"
        )

        // when
        val savedUser = userRepository.save(user)

        // then
        assertNotNull(savedUser.id)
        assertEquals("홍길동", savedUser.name)
        assertEquals("hong@example.com", savedUser.email)
    }

    @Test
    fun `이메일로 사용자를 찾을 수 있다`() {
        // given
        val user = User(
            name = "김철수",
            email = "kim@example.com"
        )
        userRepository.save(user)

        // when
        val foundUser = userRepository.findByEmail("kim@example.com")

        // then
        assertNotNull(foundUser)
        assertEquals("김철수", foundUser?.name)
        assertEquals("kim@example.com", foundUser?.email)
    }

    @Test
    fun `존재하지 않는 이메일로 조회하면 null을 반환한다`() {
        // when
        val foundUser = userRepository.findByEmail("notexist@example.com")

        // then
        assertNull(foundUser)
    }
}
