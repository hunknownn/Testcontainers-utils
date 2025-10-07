package io.testcontainers.utils.postgresql

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [PostgresqlApplication::class])
//@BootstrapTestContainer(
//    properties = [
//        ContainerProperty(component = Component.POSTGRESQL)
//    ]
//)
class BootstrapTestContainerInitializerTest2 : StringSpec({

    "test initializer" {

    }

}) {
    override fun extensions() = listOf(SpringExtension)
}