package io.testcontainers.utils.spring.bootstrap.components

object ContainerHouse {
    private val factories: MutableMap<Component, Container<*>> = listOf(
        PostgresContainerFactory(),
        // RedisContainerFactory(),
        // KafkaContainerFactory(),
    ).associateBy { it.component }
        .toMutableMap()


    fun getFactory(component: Component): Container<*> =
        factories[component] ?: error("No factory registered for $component")

    fun getFactoryOrAdd(component: Component, container: Container<*>): Container<*> =
        factories.getOrElse(component) {
            factories[component] = container
            container
        }
}