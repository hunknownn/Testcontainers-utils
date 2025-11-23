description = "Spring Boot example using testcontainers-utils library"

// 예제 애플리케이션이므로 bootJar 활성화
tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.kotlin.reflect)

    // PostgreSQL
    runtimeOnly(libs.postgresql)

    // Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.testcontainers.postgresql)

    // 로컬 프로젝트 모듈 사용
    testImplementation(projects.core)
    testImplementation(projects.postgresql)

    // Kotest
    testImplementation(libs.bundles.kotest)
}
