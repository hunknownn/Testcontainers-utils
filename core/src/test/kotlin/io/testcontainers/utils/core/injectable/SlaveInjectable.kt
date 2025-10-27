package io.testcontainers.utils.core.injectable

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.testcontainers.containers.GenericContainer

class SlaveInjectable : AbstractContainerPropertyInjector<GenericContainer<*>>() {

    override val name: String = "slave-sources"

    override fun inject(container: GenericContainer<*>, environment: ConfigurableEnvironment) {
        val properties = mutableMapOf<String, Any>()
        properties["slave.generic.image"] = container.dockerImageName
        properties["slave.generic.container-id"] = container.containerId

        val propertySource = MapPropertySource(name, properties)

        inject(environment, propertySource)
    }
}