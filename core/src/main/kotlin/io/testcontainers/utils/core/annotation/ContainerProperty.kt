package io.testcontainers.utils.core.annotation

import io.testcontainers.utils.core.core.Component
import io.testcontainers.utils.core.core.Container
import io.testcontainers.utils.core.core.Recycle
import io.testcontainers.utils.core.customizer.ContainerCustomizer
import io.testcontainers.utils.core.customizer.NoopCustomizer
import io.testcontainers.utils.core.injectable.NoopContainerPropertyInjector
import io.testcontainers.utils.core.injectable.ContainerPropertyInjector
import kotlin.reflect.KClass

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainerProperty(
    val value: String = "",
    val component: Component = Component.NONE,
    val recycle: Recycle = Recycle.NEW,
    val factoryHint: KClass<out Container<*>> = Nothing::class,
    val image: String = "",
    val customizer: KClass<out ContainerCustomizer<*>> = NoopCustomizer::class,
    val injectable: KClass<out ContainerPropertyInjector<*>> = NoopContainerPropertyInjector::class,
)