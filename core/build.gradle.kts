description = "core"

dependencies {
    // 공통 의존성
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // [Kotest]
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

    // TestContainers 관련 의존성
    implementation("org.springframework:spring-test:6.2.10")
    implementation("org.springframework.boot:spring-boot-testcontainers")
    implementation("org.testcontainers:junit-jupiter")
    implementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:mongodb")
}