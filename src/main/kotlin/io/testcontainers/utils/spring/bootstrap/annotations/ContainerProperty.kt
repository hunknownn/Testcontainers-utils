package io.testcontainers.utils.spring.bootstrap.annotations

import io.testcontainers.utils.spring.bootstrap.components.Component
import io.testcontainers.utils.spring.bootstrap.components.Container
import kotlin.reflect.KClass

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainerProperty(
    val component: Component,
    val factory: KClass<out Container<*>> = Nothing::class,
    val image: String = "",
    val reuse: Boolean = false
)