package io.testcontainers.utils.core.injectable

import org.springframework.core.env.MapPropertySource

interface Injectable<T> {
    fun inject(environment: T, property: MapPropertySource)
}