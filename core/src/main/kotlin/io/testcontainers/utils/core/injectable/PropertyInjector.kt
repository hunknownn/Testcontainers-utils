package io.testcontainers.utils.core.injectable

import org.springframework.core.env.ConfigurableEnvironment
import org.testcontainers.containers.GenericContainer

interface PropertyInjector<T : GenericContainer<*>> : Injectable<ConfigurableEnvironment> {

    fun inject(container: T, environment: ConfigurableEnvironment)
}