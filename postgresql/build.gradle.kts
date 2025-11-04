description = "PostgreSQL Testcontainers utilities"

// 라이브러리 모듈 설정
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
    archiveClassifier = ""
}

mavenPublishing {
    coordinates(group.toString(), "testcontainers-utils-postgresql", version.toString())

    pom {
        name.set("Testcontainers Utils PostgreSQL")
        description.set(project.description)
    }
}

dependencies {
    // Core module
    implementation(project(":core"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework:spring-test:6.2.10")

    // Testcontainers
    implementation("org.testcontainers:postgresql")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
}
