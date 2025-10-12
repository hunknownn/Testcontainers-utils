package io.testcontainers.utils.core.annotation

import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.Container
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import io.testcontainers.utils.core.customizer.NoopCustomizer
import kotlin.reflect.KClass

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainerProperty(
    val component: Component,
    val factoryHint: KClass<out Container<*>> = Nothing::class,
    val image: String = "",
    val customizer: KClass<out ContainerCustomizer<*>> = NoopCustomizer::class
)