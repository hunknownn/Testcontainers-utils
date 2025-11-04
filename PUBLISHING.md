# Maven Central 배포 가이드

이 문서는 testcontainers-utils 라이브러리를 Maven Central에 배포하는 방법을 설명합니다.

## 사전 준비

### 1. Central Portal 계정 생성
1. https://central.sonatype.com 접속
2. GitHub 계정으로 로그인 (권장)

### 2. Namespace 등록
1. Central Portal에서 **Namespaces** 메뉴 클릭
2. `io.github.hunknownn` namespace 추가
3. GitHub 소유권 자동 검증

### 3. User Token 생성
1. 우측 상단 계정 → **View Account**
2. **Generate User Token** 클릭
3. Username과 Password 복사

### 4. GPG 키 생성

```bash
# GPG 설치 (macOS)
brew install gnupg

# GPG 키 생성
gpg --gen-key

# 키 ID 확인 (마지막 8자리 사용)
gpg --list-keys

# Secret key ring export
gpg --export-secret-keys -o ~/.gnupg/secring.gpg
```

### 5. gradle.properties 설정

프로젝트 루트 또는 `~/.gradle/gradle.properties`에 설정:

```properties
# Maven Central (Central Portal) Credentials
mavenCentralUsername=YOUR_TOKEN_USERNAME
mavenCentralPassword=YOUR_TOKEN_PASSWORD

# GPG Signing Configuration
signing.keyId=YOUR_KEY_ID_LAST_8_CHARS
signing.password=YOUR_GPG_PASSPHRASE
signing.secretKeyRingFile=/Users/yourusername/.gnupg/secring.gpg
```

**⚠️ 보안**: `gradle.properties`는 절대 Git에 커밋하지 마세요!

## 배포 프로세스

### 1. 버전 업데이트

`build.gradle.kts`에서 버전 변경:

```kotlin
allprojects {
    group = "io.github.hunknownn"
    version = "0.2.0"  // 원하는 버전으로 변경
}
```

### 2. 빌드 및 테스트

```bash
./gradlew clean build test
```

### 3. Maven Central에 배포

```bash
./gradlew publishAllPublicationsToMavenCentralRepository
```

이 명령어는:
- 모든 모듈 빌드
- Sources/Javadoc JAR 생성
- GPG 서명
- Central Portal에 업로드

### 4. Central Portal에서 Publish

1. https://central.sonatype.com 로그인
2. **Deployments** 메뉴 클릭
3. 업로드된 deployment 확인 (상태: PUBLISHING 또는 VALIDATED)
4. Deployment 선택
5. 검증 완료 후 **"Publish"** 버튼 클릭

### 5. Maven Central 동기화 대기

- 동기화 시간: 10분 ~ 2시간
- 검색: https://search.maven.org
- 완전 동기화: 최대 24시간

## 사용 방법

배포 완료 후 다른 프로젝트에서 사용:

```kotlin
dependencies {
    implementation("io.github.hunknownn:testcontainers-utils-core:0.1.1")
    implementation("io.github.hunknownn:testcontainers-utils-postgresql:0.1.1")
}
```

## 유용한 Gradle 태스크

```bash
# Publishing 관련 태스크 확인
./gradlew tasks --group publishing

# 로컬 Maven 저장소에 배포 (테스트용)
./gradlew publishToMavenLocal

# 특정 모듈만 배포
./gradlew :core:publishAllPublicationsToMavenCentralRepository
./gradlew :postgresql:publishAllPublicationsToMavenCentralRepository
```

## 트러블슈팅

### GPG 서명 실패

```bash
# GPG 키 확인
gpg --list-secret-keys

# GPG agent 재시작
gpgconf --kill gpg-agent
gpgconf --launch gpg-agent
```

### 인증 실패

- Central Portal User Token이 올바른지 확인
- `gradle.properties`의 credential 확인

### 배포 검증 실패

Maven Central 요구사항:
- ✅ POM에 name, description, url 필수
- ✅ License 정보 필수
- ✅ Developer 정보 필수
- ✅ SCM 정보 필수
- ✅ 모든 JAR이 GPG 서명되어야 함
- ✅ Sources 및 Javadoc JAR 필수

## 참고 자료

- [Central Portal 가이드](https://central.sonatype.org/publish/publish-portal-gradle/)
- [vanniktech 플러그인 문서](https://github.com/vanniktech/gradle-maven-publish-plugin)
- [Maven Central 요구사항](https://central.sonatype.org/pages/requirements/)
