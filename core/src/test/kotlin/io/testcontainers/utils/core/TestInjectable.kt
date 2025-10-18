package io.testcontainers.utils.core

import io.testcontainers.utils.core.injectable.PropertyInjector
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.testcontainers.containers.GenericContainer

class TestInjectable: PropertyInjector<GenericContainer<*>> {
    override fun inject(container: GenericContainer<*>, environment: ConfigurableEnvironment) {
        val properties = mutableMapOf<String, Any>()
        properties["testcontainers.generic.image"] = container.dockerImageName
        properties["testcontainers.generic.container-id"] = container.containerId


        val propertySource = MapPropertySource("testcontainers-utils", properties)

        inject(environment, propertySource)
    }

    override fun <C> inject(environment: ConfigurableEnvironment, property: PropertySource<C>) {
        environment.propertySources.addFirst(property)
    }
}