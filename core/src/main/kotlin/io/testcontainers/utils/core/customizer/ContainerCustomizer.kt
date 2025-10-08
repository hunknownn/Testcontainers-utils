package io.testcontainers.utils.core.customizer

import org.testcontainers.containers.GenericContainer

fun interface ContainerCustomizer<in T : GenericContainer<*>> {
    fun customize(container: T)
}
