package io.testcontainers.utils.core.customizer

import org.testcontainers.containers.GenericContainer

class NoopCustomizer : ContainerCustomizer<GenericContainer<*>> {
    override fun customize(container: GenericContainer<*>) {}
}
