package io.testcontainers.utils.core.core

import org.testcontainers.containers.GenericContainer

class NoopContainer : AbstractContainer<GenericContainer<*>>() {

    override val component: Component = Component.NONE

    override fun supports(): Component = component

    override fun container(): GenericContainer<*> {
        return NoopGenericContainer(component.defaultImage)
    }

    override fun container(image: String): GenericContainer<*> {
        return NoopGenericContainer(image)
    }

    override fun customize(container: GenericContainer<*>) = Unit
}

class NoopGenericContainer(image: String) : GenericContainer<NoopGenericContainer>(image) {
    override fun start() = Unit
}