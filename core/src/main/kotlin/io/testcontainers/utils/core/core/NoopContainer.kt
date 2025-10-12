package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import org.springframework.core.env.ConfigurableEnvironment
import org.testcontainers.containers.GenericContainer

class NoopContainer : Container<GenericContainer<*>> {

    override val component: Component = Component.NONE

    override fun recycle(): Recycle = Recycle.NEW

    override fun supports(): Component = component

    override fun injectProperties(container: GenericContainer<*>, environment: ConfigurableEnvironment) = Unit

    override fun customize(container: GenericContainer<*>) = Unit

    override fun container(image: String, customizer: ContainerCustomizer<GenericContainer<*>>): GenericContainer<*> {
        class NoopGenericContainer(image: String) : GenericContainer<NoopGenericContainer>(image) {
            override fun start() = Unit
        }
        return NoopGenericContainer(image)
    }
}