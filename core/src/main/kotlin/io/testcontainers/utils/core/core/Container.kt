package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import io.testcontainers.utils.core.injectable.PropertyInjector
import org.testcontainers.containers.GenericContainer

interface Container<SELF : GenericContainer<*>> {
    val component: Component

    fun supports(): Component

    fun container(): SELF

    fun container(image: String): SELF

    fun containerShell(
        recycle: Recycle,
        customizer: ContainerCustomizer<SELF>?,
        injectable: PropertyInjector<SELF>?
    ): ContainerConfiguration<SELF>

    fun containerShell(
        image: String,
        recycle: Recycle,
        customizer: ContainerCustomizer<SELF>?,
        injectable: PropertyInjector<SELF>?
    ): ContainerConfiguration<SELF>

    fun customize(container: SELF)
}

enum class Recycle {
    NEW,
    MERGE,
    ;
}