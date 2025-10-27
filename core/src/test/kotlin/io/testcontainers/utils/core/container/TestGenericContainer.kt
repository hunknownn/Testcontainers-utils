package io.testcontainers.utils.core.container

import io.testcontainers.utils.core.core.AbstractContainer
import io.testcontainers.utils.core.core.Component
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class TestGenericContainer : AbstractContainer<GenericContainer<*>>() {
    override val component: Component = Component.NONE

    override fun supports(): Component = component

    override fun container(): GenericContainer<*> {
        return GenericContainer(DockerImageName.parse("alpine:latest"))
    }

    override fun container(image: String): GenericContainer<*> {
        val dockerImageName = image.takeIf { it.isNotBlank() } ?: "alpine:latest"
        return GenericContainer(DockerImageName.parse(dockerImageName))
    }

    override fun customize(container: GenericContainer<*>) {
        container.apply {
            withLabel("managed-by", "testcontainers-utils")
            withLabel("factory", "TestGenericContainer")
            withEnv("TEST_ENV", "test-value")
        }
    }
}
