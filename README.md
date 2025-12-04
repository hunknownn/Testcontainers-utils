# Testcontainers-Utils

[![Maven Central - Core](https://img.shields.io/maven-central/v/io.github.hunknownn/testcontainers-utils-core?label=core)](https://central.sonatype.com/artifact/io.github.hunknownn/testcontainers-utils-core)
[![Maven Central - PostgreSQL](https://img.shields.io/maven-central/v/io.github.hunknownn/testcontainers-utils-postgresql?label=postgresql)](https://central.sonatype.com/artifact/io.github.hunknownn/testcontainers-utils-postgresql)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.25-blue.svg?logo=kotlin)](http://kotlinlang.org)

> Spring Test에서 Testcontainers를 선언적이고 자동화된 방식으로 관리하는 라이브러리

## 개요

Testcontainers-Utils는 Spring Boot 테스트 환경에서 Testcontainers를 쉽고 효율적으로 사용할 수 있도록 도와주는 라이브러리입니다. 단순히 어노테이션을 추가하는 것만으로 컨테이너를 자동으로 시작하고, 환경 변수를 주입하며, 여러 테스트에서 컨테이너를 재사용할 수 있습니다.

### 주요 기능

- **선언적 컨테이너 관리**: `@BootstrapTestcontainers` 어노테이션만으로 컨테이너 자동 시작
- **자동 환경 변수 주입**: 컨테이너 정보를 Spring Environment에 자동 주입
- **컨테이너 재사용**: 동일 구성의 컨테이너를 여러 테스트에서 재사용하여 테스트 속도 향상
- **확장 가능한 아키텍처**: 모든 Testcontainers 지원 가능
- **Spring Test 통합**: ApplicationContext 초기화 전 컨테이너 시작으로 완벽한 통합

## 빠른 시작

### 의존성 추가

**Gradle (Kotlin DSL)**
```kotlin
dependencies {
   testImplementation("io.github.hunknownn:testcontainers-utils-core:0.1.4")
   testImplementation("io.github.hunknownn:testcontainers-utils-postgresql:0.1.4")
   testImplementation("io.github.hunknownn:testcontainers-utils-redis:0.1.4")
}
```

**Gradle (Groovy DSL)**
```groovy
dependencies {
   testImplementation 'io.github.hunknownn:testcontainers-utils-core:0.1.4'
   testImplementation 'io.github.hunknownn:testcontainers-utils-postgresql:0.1.4'
   testImplementation 'io.github.hunknownn:testcontainers-utils-redis:0.1.4'
}
```

**Maven**
```xml
<dependency>
    <groupId>io.github.hunknownn</groupId>
    <artifactId>testcontainers-utils-core</artifactId>
   <version>0.1.4</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.github.hunknownn</groupId>
    <artifactId>testcontainers-utils-postgresql</artifactId>
    <version>0.1.4</version>
    <scope>test</scope>
</dependency>
<dependency>
<groupId>io.github.hunknownn</groupId>
<artifactId>testcontainers-utils-postgresql</artifactId>
<version>0.1.4</version>
<scope>test</scope>
</dependency>
```

## 주요 개념

### 컨테이너 재사용 전략

#### Recycle.MERGE (권장)
동일한 구성의 컨테이너를 여러 테스트에서 재사용합니다. 컨테이너 구성(이미지, 설정, 커스터마이저)을 해시로 비교하여 중복 실행을 방지합니다.

```kotlin
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            component = Component.POSTGRESQL,
            recycle = Recycle.MERGE,  // 기본값
            image = "postgres:16"
        )
    ]
)
```

**장점:**
- 테스트 실행 시간 단축
- 리소스 효율적 사용

#### Recycle.NEW
매번 새로운 컨테이너를 생성합니다. 테스트 간 완전한 격리가 필요할 때 사용합니다.

```kotlin
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            component = Component.POSTGRESQL,
            recycle = Recycle.NEW,
            image = "postgres:16"
        )
    ]
)
```

**사용 시나리오:**
- 테스트 간 데이터 격리가 중요한 경우
- 컨테이너 상태 변경이 다른 테스트에 영향을 줄 수 있는 경우

### 컨테이너 지정 방법

#### 1. Component로 지정
사전 정의된 컴포넌트 타입을 사용합니다.

```kotlin
ContainerProperty(
    component = Component.POSTGRESQL,
    image = "postgres:16"
)
```

#### 2. FactoryHint로 지정
커스텀 Factory 클래스를 직접 지정합니다.

```kotlin
ContainerProperty(
    factoryHint = MyCustomContainerFactory::class,
    image = "custom-image:latest"
)
```

## 고급 사용법

### 커스텀 컨테이너 팩토리 만들기

```kotlin
class RedisContainerFactory : AbstractContainer<GenericContainer<*>>() {
    override val component = Component.REDIS

    override fun container(image: String): GenericContainer<*> {
        return GenericContainer(DockerImageName.parse(image))
            .withExposedPorts(6379)
    }

    override fun customize(container: GenericContainer<*>) {
        container.withCommand("redis-server", "--appendonly", "yes")
    }
}
```

### spring.factories에 팩토리 등록

`src/main/resources/META-INF/spring.factories`:

```properties
io.testcontainers.utils.core.core.Container=\
  com.example.RedisContainerFactory
```

### 여러 컨테이너 동시 사용

```kotlin
@SpringBootTest
@BootstrapTestcontainers(
    properties = [
        ContainerProperty(
            value = "master-db",
            component = Component.POSTGRESQL,
            image = "postgres:16",
            customizer = MasterDbCustomizer::class,
            injectable = MasterDbInjectable::class
        ),
        ContainerProperty(
            value = "slave-db",
            component = Component.POSTGRESQL,
            image = "postgres:16",
            customizer = SlaveDbCustomizer::class,
            injectable = SlaveDbInjectable::class
        )
    ]
)
class MultiDatabaseTest {
    // Master-Slave 구조 테스트
}
```

## 동작 원리

### Spring Test 통합 방식

이 라이브러리는 Spring Test Framework의 확장 포인트를 활용하여 동작합니다:

1. **ContainerDiscovery (TestExecutionListener)**
   - spring.factories에서 Container 구현체를 자동 검색
   - ContainerRegistry에 팩토리 등록

2. **TestcontainersContextCustomizerFactory**
   - `@BootstrapTestcontainers` 어노테이션 감지
   - TestcontainersContextCustomizer 생성

3. **TestcontainersContextCustomizer**
   - ApplicationContext refresh 전에 실행
   - 컨테이너 시작 및 환경 변수 주입

```
@SpringBootTest 실행
    ↓
ContainerDiscovery.beforeTestClass()
    └─> Container 팩토리 자동 검색 및 등록
    ↓
TestcontainersContextCustomizerFactory
    └─> @BootstrapTestcontainers 감지
    ↓
TestcontainersContextCustomizer.customizeContext()
    └─> 컨테이너 시작 (refresh 전!)
    └─> 환경 변수 주입
    ↓
ApplicationContext refresh
    └─> 주입된 환경 변수로 빈 초기화
```

## 요구사항

- **Java**: 17 이상
- **Kotlin**: 1.9.25 이상
- **Spring Boot**: 3.x
- **Testcontainers**: 2.x

## 라이선스

이 프로젝트는 [Apache License 2.0](LICENSE) 라이선스로 배포됩니다.
