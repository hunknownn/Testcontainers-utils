description = "Spring Boot example using testcontainers-utils PostgreSQL module"

// 예제 애플리케이션이므로 bootJar 활성화
tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.starter.data.jpa)

    // PostgreSQL
    runtimeOnly(libs.postgresql)

    // Testcontainers
    testImplementation(libs.testcontainers.postgresql)

    // 로컬 프로젝트 모듈 사용
    testImplementation(projects.core)
    testImplementation(projects.postgresql)
}

// Example 모듈은 배포하지 않음
tasks.withType<PublishToMavenRepository>().configureEach { enabled = false }
tasks.withType<PublishToMavenLocal>().configureEach { enabled = false }
