package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.customizer.ContainerCustomizer
import io.testcontainers.utils.core.injectable.ContainerPropertyInjector
import org.testcontainers.containers.GenericContainer

abstract class AbstractContainer<T : GenericContainer<*>> : Container<T> {

    override fun containerShell(
        recycle: Recycle,
        customizer: ContainerCustomizer<T>?,
        injectable: ContainerPropertyInjector<T>?
    ): ContainerConfiguration<T> {
        return containerShell(component.defaultImage, recycle, customizer, injectable)
    }

    override fun containerShell(
        image: String,
        recycle: Recycle,
        customizer: ContainerCustomizer<T>?,
        injectable: ContainerPropertyInjector<T>?
    ): ContainerConfiguration<T> {
        return ContainerConfiguration(
            component = component,
            recycle = recycle,
            factoryHint = this::class,
            container(image),
            customize = {
                customize(this)
                customizer?.customize(this)
            },
            injectable = { container, environment ->
                injectable?.inject(container, environment)
            }
        )
    }
}