package io.testcontainers.utils.annotation

import io.testcontainers.utils.core.Component
import io.testcontainers.utils.core.Container
import io.testcontainers.utils.customizer.ContainerCustomizer
import io.testcontainers.utils.customizer.NoopCustomizer
import kotlin.reflect.KClass

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainerProperty(
    val component: Component,
    val factory: KClass<out Container<*>> = Nothing::class,
    val image: String = "",
    val customizer: KClass<out ContainerCustomizer<*>> = NoopCustomizer::class
)