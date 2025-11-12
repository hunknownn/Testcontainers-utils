plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.5.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.vanniktech.maven.publish") version "0.28.0" apply false
}

allprojects {
    group = "io.github.hunknownn"
    version = "0.1.2"

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
