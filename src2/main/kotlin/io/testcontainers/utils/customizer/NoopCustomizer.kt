package io.testcontainers.utils.customizer

import org.testcontainers.containers.GenericContainer

class NoopCustomizer : ContainerCustomizer<GenericContainer<*>> {
    override fun customize(container: GenericContainer<*>) {}
}
