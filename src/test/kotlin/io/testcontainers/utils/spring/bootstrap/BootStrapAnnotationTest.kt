package io.testcontainers.utils.spring.bootstrap

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.testcontainers.utils.spring.bootstrap.annotations.BootstrapTestContainer
import io.testcontainers.utils.spring.bootstrap.components.Component
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.reflect.full.findAnnotation

class BootStrapAnnotationTest : StringSpec({

    "test find BootstrapTestContainer member" {

        @BootstrapTestContainer(
            components = [Component.POSTGRESQL]
        )
        class PostgresComponent

        val annotation = PostgresComponent::class.findAnnotation<BootstrapTestContainer>()
        annotation!!.components.forEach {
            Component.entries.contains(it) shouldBe true
        }
    }

    "test BootstrapTestContainer.Component.exec" {

        @BootstrapTestContainer(
            components = [Component.POSTGRESQL]
        )
        class PostgresComponent

        val testClass = PostgresComponent::class
        val annotation = testClass.findAnnotation<BootstrapTestContainer>()

        annotation.shouldNotBeNull()
        annotation shouldBe instanceOf<BootstrapTestContainer>()

        annotation.components.forEach { component ->
            when (component) {
                Component.POSTGRESQL -> {
                    val dockerImageName = DockerImageName.parse("postgres:16")
                    val container = component.exec(dockerImageName)

                    // 컨테이너 타입 검증
                    container shouldBe instanceOf<PostgreSQLContainer<*>>()

                    // Docker 이미지 검증 (리플렉션 사용)
                    val remoteImage = container.image

                    // RemoteDockerImage에서 imageNameFuture 필드 접근
                    val imageNameFutureField = remoteImage::class.java.getDeclaredField("imageNameFuture")
                    imageNameFutureField.isAccessible = true
                    @Suppress("UNCHECKED_CAST")
                    val imageNameFuture =
                        imageNameFutureField.get(remoteImage) as java.util.concurrent.Future<DockerImageName>

                    // Future에서 실제 DockerImageName 추출
                    val extractedImageName = imageNameFuture.get()

                    // 이미지 정보 검증
                    extractedImageName.toString() shouldBe "postgres:16"
                    extractedImageName.repository shouldBe "postgres"
                    extractedImageName.versionPart shouldBe "16"
                }
            }
        }
    }

    "test BootstrapTestContainer.Component.exec when empty" {
        @BootstrapTestContainer(
            components = []
        )
        class EmptyComponent

        val annotation = EmptyComponent::class.findAnnotation<BootstrapTestContainer>()

        annotation.shouldNotBeNull()
        annotation shouldBe instanceOf<BootstrapTestContainer>()

        annotation.components shouldBe emptyArray<Component>()

        // exec() throw exception when components is empty
    }

}) {
    override fun extensions() = listOf(SpringExtension)
}