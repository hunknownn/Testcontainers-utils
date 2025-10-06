package io.testcontainers.utils.core.core

import io.testcontainers.utils.core.factory.PostgresContainerFactory

object ContainerRegistry {
    private val factories: MutableMap<Component, Container<*>> = mutableMapOf()

    init {
        factories[Component.POSTGRESQL] = PostgresContainerFactory()
    }

    fun getFactory(component: Component): Container<*> =
        factories[component] ?: error("No factory registered for $component")

    fun getFactoryOrAdd(component: Component, container: Container<*>): Container<*> =
        factories.getOrElse(component) {
            factories[component] = container
            container
        }
}