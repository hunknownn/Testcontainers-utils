package io.testcontainers.utils.spring.bootstrap.components

object ContainerHouse {
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