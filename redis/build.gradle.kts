description = "Redis Testcontainers utilities"

// 라이브러리 모듈 설정
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
    archiveClassifier = ""
}

mavenPublishing {
    coordinates(group.toString(), "testcontainers-utils-redis", version.toString())

    pom {
        name.set("Testcontainers Utils Redis")
        description.set(project.description)
    }
}

dependencies {
    // Core module
    implementation(projects.core)

    // Spring
    implementation(libs.spring.test)

    // Testcontainers
    implementation(libs.testcontainers.redis)
}
