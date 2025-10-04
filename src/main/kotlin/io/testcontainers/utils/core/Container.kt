package io.testcontainers.utils.core

import io.testcontainers.utils.customizer.ContainerCustomizer
import org.testcontainers.containers.GenericContainer

interface Container<SELF : GenericContainer<*>> {
    val component: Component

    fun recycle(): Recycle
    fun supports(): Component
    fun container(image: String, customizer: ContainerCustomizer<GenericContainer<*>>): SELF
}

enum class Recycle {
    NEW,
    MERGE,
    ;
}