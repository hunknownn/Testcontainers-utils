package io.testcontainers.utils.core.injectable

import org.springframework.core.env.PropertySource

interface Injectable<T> {
    fun <C> inject(environment: T, property: PropertySource<C>)
}