package io.testcontainers.utils.spring.bootstrap.annotations

import io.testcontainers.utils.spring.bootstrap.components.Component

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainerProperty(
    val component: Component,
    val image: String = "",
    val reuse: Boolean = false
)