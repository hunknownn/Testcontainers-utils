package io.testcontainers.utils.core.annotation

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class BootstrapTestcontainers(
    val properties: Array<ContainerProperty> = []
)

