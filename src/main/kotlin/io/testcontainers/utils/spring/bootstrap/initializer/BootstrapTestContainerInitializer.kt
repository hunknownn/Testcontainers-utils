package io.testcontainers.utils.spring.bootstrap.initializer

import io.testcontainers.utils.spring.bootstrap.components.Container
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter

class BootstrapTestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        logger.info("##########################################")
        logger.info("Bootstrap TestContainer Initializer started")
        logger.info("##########################################")
        println("### BootstrapTestContainerInitializer STARTED ###")
        try {
            val provider = ClassPathScanningCandidateComponentProvider(false, applicationContext.environment)
            // 인터페이스 기반 필터 추가
            val interfaceFilter = AssignableTypeFilter(Container::class.java)
            provider.addIncludeFilter(interfaceFilter)

            // 전체 패키지를 검색 대상으로 확장
            val basePackage = "io.testcontainers.utils"
            val components = provider.findCandidateComponents(basePackage)

            if (components.isEmpty()) {
                logger.warn("No Container implementations found in package: $basePackage")
            } else {
                logger.info("Found ${components.size} container implementations:")
                components.forEach { beanDefinition ->
                    logger.info("Container component: ${beanDefinition.beanClassName}")
                    // 여기서 필요한 컨테이너 로직 구현
                    // 예: 컨테이너 인스턴스 생성 및 시작
                }
            }
        } catch (e: Exception) {
            logger.error("Error during TestContainer initialization", e)
        }
    }
}