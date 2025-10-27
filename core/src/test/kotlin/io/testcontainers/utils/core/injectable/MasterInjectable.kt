package io.testcontainers.utils.core.injectable

import org.springframework.core.env.*
import org.testcontainers.containers.GenericContainer

class MasterInjectable : AbstractContainerPropertyInjector<GenericContainer<*>>() {
    override val name = "master-sources"

    override fun inject(container: GenericContainer<*>, environment: ConfigurableEnvironment) {
        val properties = mutableMapOf<String, Any>()
        properties["master.generic.image"] = container.dockerImageName
        properties["master.generic.container-id"] = container.containerId

        val propertySource = MapPropertySource(name, properties)

        inject(environment, propertySource)
    }
}