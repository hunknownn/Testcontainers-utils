package io.testcontainers.utils.core.customizer

import org.testcontainers.containers.GenericContainer

class TestAlpineCustomizer : ContainerCustomizer<GenericContainer<*>> {
    override fun customize(container: GenericContainer<*>) {
        container.apply {
            withLabel("test-type", "customizer")
            withLabel("container-type", "alpine")
            withLabel("purpose", "generic-test")
            withLabel("version", "1.0.0")
            withCommand("sleep", "30000") // test가 동작하기 위해 30초 동안 sleep하여 컨테이너가 죽지 않도록 강제
        }
    }
}