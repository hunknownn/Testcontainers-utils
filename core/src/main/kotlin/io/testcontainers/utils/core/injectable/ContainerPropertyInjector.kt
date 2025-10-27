package io.testcontainers.utils.core.injectable

import org.springframework.core.env.ConfigurableEnvironment
import org.testcontainers.containers.GenericContainer

interface ContainerPropertyInjector<T : GenericContainer<*>> : Injectable<ConfigurableEnvironment> {

    val name: String

    fun inject(container: T, environment: ConfigurableEnvironment)
}