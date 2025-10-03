package io.testcontainers.utils.spring.bootstrap.components

import org.testcontainers.containers.GenericContainer

interface Container<SELF : GenericContainer<*>> {
    val component: Component

    fun recycle(): Recycle
    fun supports(): Component
    fun container(image: String, reuse: Boolean): SELF
}

enum class Recycle {
    NEW,
    MERGE,
    ;
}