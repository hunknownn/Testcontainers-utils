description = "modules"

plugins {
    kotlin("jvm")
}

dependencies {
    // core 모듈 의존성
    implementation(project(":core"))
}

tasks.register("prepareKotlinBuildScriptModel") {}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}