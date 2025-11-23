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
    implementation(projects.core)

    // Spring
    implementation(libs.spring.test)

    // Testcontainers
    implementation(libs.testcontainers.postgresql)
}
