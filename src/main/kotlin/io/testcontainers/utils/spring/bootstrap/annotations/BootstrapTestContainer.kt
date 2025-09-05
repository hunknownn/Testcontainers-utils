package io.testcontainers.utils.spring.bootstrap.annotations

import io.testcontainers.utils.spring.bootstrap.components.Component


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BootstrapTestContainer(
    val components: Array<Component>
)
