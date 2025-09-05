package io.testcontainers.utils.spring

import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration
