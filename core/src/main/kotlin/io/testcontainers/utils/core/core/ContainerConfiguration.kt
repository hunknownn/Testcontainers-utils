package io.testcontainers.utils.core.core

import org.springframework.core.env.ConfigurableEnvironment
import org.testcontainers.containers.GenericContainer
import java.util.Objects
import kotlin.math.absoluteValue
import kotlin.reflect.KClass

class ContainerConfiguration<T : GenericContainer<*>>(
    val component: Component,
    val recycle: Recycle,
    val factoryHint: KClass<out Container<T>>,
    val container: T,
    val customize: T.() -> Unit,
    val injectable: (T, ConfigurableEnvironment) -> Unit,
) {

    override fun hashCode(): Int = when (recycle) {
        Recycle.NEW -> System.identityHashCode(this)
        Recycle.MERGE -> Objects.hash(
            component,
            recycle,
            container.dockerImageName,
            container.javaClass,
            customize.javaClass,
            injectable.javaClass
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContainerConfiguration<*>) return false
        if (recycle == Recycle.NEW || other.recycle == Recycle.NEW) return false

        return component == other.component &&
            recycle == other.recycle &&
            container.dockerImageName == other.container.dockerImageName &&
            container.javaClass == other.container.javaClass &&
            customize.javaClass == other.customize.javaClass &&
            injectable.javaClass == other.injectable.javaClass
    }

    fun key(value:String) : String {
        if(value.isNotBlank()) return "$value#${container.dockerImageName}"
        return "${factoryHint.simpleName}#${hashCode().absoluteValue}"
    }

    fun customizeContainer() {
        customize.invoke(container)
    }

    fun inject(environment: ConfigurableEnvironment) {
        injectable.invoke(container, environment)
    }
}