package io.testcontainers.utils.core.injectable

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.testcontainers.containers.GenericContainer

class NoopContainerPropertyInjector : AbstractContainerPropertyInjector<GenericContainer<*>>() {

    override val name: String = "noop-injector"

    override fun inject(container: GenericContainer<*>, environment: ConfigurableEnvironment) {}
}