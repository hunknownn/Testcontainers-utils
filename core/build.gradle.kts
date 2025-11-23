description = "Core utilities for Testcontainers integration"

// 라이브러리 모듈 설정
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
    archiveClassifier = ""
}

mavenPublishing {
    coordinates(group.toString(), "testcontainers-utils-core", version.toString())

    pom {
        name.set("Testcontainers Utils Core")
        description.set(project.description)
    }
}

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.starter)
    implementation(libs.kotlin.reflect)
    implementation(libs.spring.test)

    // Testcontainers
    implementation(libs.spring.boot.testcontainers)
    implementation(libs.testcontainers.junit.jupiter)
    implementation(libs.testcontainers.postgresql)

    // Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Kotest
    testImplementation(libs.bundles.kotest)

    // Test containers
    testImplementation(libs.testcontainers.mongodb)
}
