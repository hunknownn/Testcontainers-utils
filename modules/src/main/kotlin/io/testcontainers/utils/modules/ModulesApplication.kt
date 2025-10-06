package io.testcontainers.utils.modules

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ModulesApplication

fun main(args: Array<String>) {
    runApplication<ModulesApplication>(*args)
}
