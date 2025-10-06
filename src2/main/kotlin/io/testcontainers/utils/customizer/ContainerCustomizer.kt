package io.testcontainers.utils.customizer

import org.testcontainers.containers.GenericContainer

fun interface ContainerCustomizer<T : GenericContainer<*>> {
    fun customize(container: T)
}
