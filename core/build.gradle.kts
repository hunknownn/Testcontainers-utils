description = "core"

plugins {
    kotlin("jvm")
}

dependencies {
    // TestContainers 관련 의존성
    implementation("org.springframework:spring-test:6.2.10")
    implementation("org.springframework.boot:spring-boot-testcontainers")
    implementation("org.testcontainers:junit-jupiter")
    implementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:mongodb")
}

tasks.register("prepareKotlinBuildScriptModel") {}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}