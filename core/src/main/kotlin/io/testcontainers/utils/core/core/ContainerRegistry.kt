package io.testcontainers.utils.core.core

import org.testcontainers.containers.GenericContainer

object ContainerRegistry {
    private val factories: MutableMap<Component, Container<out GenericContainer<*>>> = mutableMapOf()

    fun <T : GenericContainer<*>> register(component: Component, container: Container<T>) {
        this.factories[component] = container
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : GenericContainer<*>> getFactory(component: Component): Container<T> =
        factories[component] as? Container<T>
            ?: error("No factory registered for $component")

    @Suppress("UNCHECKED_CAST")
    fun <T : GenericContainer<*>> getFactoryOrAdd(component: Component, container: Container<*>): Container<T> =
        factories.getOrPut(component) { container } as Container<T>

}