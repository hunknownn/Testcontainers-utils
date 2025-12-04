plugins {
}

dependencies {
}

// 부모 모듈은 소스가 없으므로 jar 태스크 비활성화
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = false
}

// Example 모듈은 배포하지 않음
tasks.withType<PublishToMavenRepository>().configureEach { enabled = false }
tasks.withType<PublishToMavenLocal>().configureEach { enabled = false }
