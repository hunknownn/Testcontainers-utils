package io.testcontainers.utils.core.injectable

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.PropertySource
import org.testcontainers.containers.GenericContainer

class NoopPropertyInjector : PropertyInjector<GenericContainer<*>> {
    override fun inject(container: GenericContainer<*>, environment: ConfigurableEnvironment) {}

    override fun <C> inject(environment: ConfigurableEnvironment, property: PropertySource<C>) {}
}