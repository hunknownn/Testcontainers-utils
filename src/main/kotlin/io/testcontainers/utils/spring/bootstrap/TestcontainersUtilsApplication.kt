package io.testcontainers.utils.spring.bootstrap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestcontainersUtilsApplication

fun main(args: Array<String>) {
    runApplication<TestcontainersUtilsApplication>(*args)
}
