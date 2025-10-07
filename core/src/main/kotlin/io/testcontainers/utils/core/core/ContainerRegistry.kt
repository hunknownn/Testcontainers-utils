package io.testcontainers.utils.core.core

object ContainerRegistry {
    private val factories: MutableMap<Component, Container<*>> = mutableMapOf()

    fun register(component: Component, container: Container<*>) {
        this.factories[component] = container
    }

    fun getFactory(component: Component): Container<*> =
        factories[component] ?: error("No factory registered for $component")

    fun getFactoryOrAdd(component: Component, container: Container<*>): Container<*> =
        factories.getOrElse(component) {
            factories[component] = container
            container
        }
}