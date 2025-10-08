package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.testcontainers.containers.GenericContainer

interface Container<SELF : GenericContainer<*>> {
    val component: Component

    fun recycle(): Recycle
    fun supports(): Component
    fun container(image: String, customizer: ContainerCustomizer<SELF>): SELF
    fun customize(container: SELF)
}

enum class Recycle {
    NEW,
    MERGE,
    ;
}