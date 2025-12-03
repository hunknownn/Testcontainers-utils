plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.maven.publish) apply false
}

allprojects {
    group = "io.github.hunknownn"
    version = "0.1.3"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    // spring-example은 예제 프로젝트이므로 Maven 배포에서 제외
    if (project.name != "spring-example") {
        apply(plugin = "com.vanniktech.maven.publish")
    }

    // 공통 의존성
    dependencies {
        "implementation"(rootProject.libs.spring.boot.starter)
        "implementation"(rootProject.libs.kotlin.reflect)

        "testImplementation"(rootProject.libs.spring.boot.starter.test)
        "testImplementation"(rootProject.libs.kotlin.test.junit5)
        "testRuntimeOnly"(rootProject.libs.junit.platform.launcher)
        "testImplementation"(rootProject.libs.bundles.kotest)
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // Maven Central 배포 공통 설정 (spring-example 제외)
    if (project.name != "spring-example") {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
            signAllPublications()

            pom {
                url.set("https://github.com/hunknownn/Testcontainers-utils")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("hunknownn")
                        name.set("Donghun Kim")
                        email.set("zhfptm12@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/hunknownn/Testcontainers-utils.git")
                    developerConnection.set("scm:git:ssh://github.com/hunknownn/Testcontainers-utils.git")
                    url.set("https://github.com/hunknownn/Testcontainers-utils")
                }
            }
        }
    }
}
