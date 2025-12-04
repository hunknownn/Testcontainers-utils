description = "Spring Boot example using testcontainers-utils Redis module"

// 예제 애플리케이션이므로 bootJar 활성화
tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Testcontainers
    testImplementation(libs.testcontainers.redis)

    // 로컬 프로젝트 모듈 사용
    testImplementation(projects.core)
    testImplementation(projects.redis)
}

// Example 모듈은 배포하지 않음
tasks.withType<PublishToMavenRepository>().configureEach { enabled = false }
tasks.withType<PublishToMavenLocal>().configureEach { enabled = false }
