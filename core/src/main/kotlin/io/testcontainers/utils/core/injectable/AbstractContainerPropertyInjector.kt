package io.testcontainers.utils.core.injectable

import org.slf4j.LoggerFactory
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.testcontainers.containers.GenericContainer

abstract class AbstractContainerPropertyInjector<T : GenericContainer<*>> : ContainerPropertyInjector<T> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun inject(environment: ConfigurableEnvironment, property: MapPropertySource) {
        println("environment = ${environment}")
        println("property = ${property}")
        val source = property.source
        val mapPropertySource = environment.propertySources.get(name)

        if (mapPropertySource != null) { // 이미 존재하면 병합
            val properties = mapPropertySource.source as MutableMap<String, Any>
            source.forEach { (key, value) ->
                logger.debug("Merging property key: $key with value: $value into existing property source: $name")
                val old = properties[key]
                if(old != null) {
                    logger.warn("Overriding existing property key: $key from value: $old to new value: $value in property source: $name")
                }
                properties[key] = value // replace or add
            }

            val mergedPropertySource = MapPropertySource(name, properties)
            environment.propertySources.addFirst(mergedPropertySource)
        } else {
            logger.debug("Adding new property source: $name with properties: $source")
            environment.propertySources.addFirst(property)
        }
    }
}