package io.testcontainers.utils.core.core

import org.springframework.core.env.ConfigurableEnvironment
import org.testcontainers.containers.GenericContainer
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

    override fun hashCode(): Int {
        var result = component.hashCode()
        result = 31 * result + recycle.hashCode()
        result = 31 * result + container.dockerImageName.hashCode()
        result = 31 * result + container.javaClass.hashCode()

        result = 31 * result + customize.javaClass.hashCode()
        result = 31 * result + injectable.javaClass.hashCode()

        result = 31 * result + customize.toString().hashCode()
        result = 31 * result + injectable.toString().hashCode()

        if (recycle == Recycle.NEW) {
            result = 31 * result + System.nanoTime().hashCode()
            result = 31 * result + kotlin.random.Random.nextInt().hashCode()
        }

        return result
    }

    fun key() = "${factoryHint.simpleName}#${hashCode().absoluteValue}"

    fun customizeContainer() {
        println("container = ${container}")
        customize.invoke(container)
    }

    fun inject(environment: ConfigurableEnvironment) {
        injectable.invoke(container, environment)
    }
}