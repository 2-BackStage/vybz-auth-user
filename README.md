# VYBZ Auth User Service

VYBZ 플랫폼의 사용자 인증 및 OAuth 소셜 로그인을 담당하는 마이크로서비스입니다.

## 📋 목차

-   [개요](#개요)
-   [기술 스택](#기술-스택)
-   [주요 기능](#주요-기능)
-   [프로젝트 구조](#프로젝트-구조)
-   [API 문서](#api-문서)
-   [설치 및 실행](#설치-및-실행)
-   [환경 설정](#환경-설정)
-   [인증 시스템](#인증-시스템)
-   [이벤트 처리](#이벤트-처리)

## 🎯 개요

VYBZ Auth User Service는 다음과 같은 기능을 제공합니다:

-   **OAuth 소셜 로그인**: Google, Kakao 소셜 로그인 지원
-   **JWT 토큰 관리**: Access Token, Refresh Token 발급 및 검증
-   **사용자 인증**: JWT 기반 인증 및 권한 관리
-   **토큰 재발급**: Refresh Token을 통한 Access Token 재발급
-   **로그아웃**: 토큰 무효화 및 세션 관리
-   **이벤트 처리**: Kafka를 통한 사용자 생성 이벤트 발행

## 🛠 기술 스택

### Backend

![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

### Infra

![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

### 협업

![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

### Database & Cache

-   **MySQL 8.0**: 사용자 정보 및 인증 데이터 저장
-   **Redis**: Refresh Token 저장 및 세션 관리

### Message Queue

-   **Apache Kafka**: 비동기 이벤트 처리

### Documentation

-   **Swagger/OpenAPI 3.0**: API 문서화

### Build & Deploy

-   **Gradle**: 빌드 도구
-   **Docker**: 컨테이너화

## 🚀 주요 기능

### 1. OAuth 소셜 로그인 (`/api/v1/oauth`)

-   **소셜 로그인**: Google, Kakao 소셜 로그인 지원
-   **사용자 등록**: 신규 사용자 자동 등록
-   **기존 사용자 검증**: 이메일 및 Provider ID 기반 사용자 검증
-   **토큰 발급**: 로그인 성공 시 Access Token, Refresh Token 발급

### 2. 토큰 관리

-   **Access Token**: API 접근을 위한 JWT 토큰 (30분 유효)
-   **Refresh Token**: Access Token 재발급을 위한 토큰 (15일 유효)
-   **토큰 검증**: JWT 서명 및 만료 시간 검증
-   **토큰 재발급**: Refresh Token을 통한 Access Token 재발급

### 3. 인증 및 보안

-   **JWT 인증 필터**: 모든 API 요청에 대한 JWT 토큰 검증
-   **Spring Security**: 인증 및 권한 관리
-   **CORS 설정**: 크로스 오리진 요청 허용
-   **세션 관리**: Stateless 세션 관리

### 4. 로그아웃

-   **토큰 무효화**: Refresh Token Redis에서 삭제
-   **세션 종료**: 사용자 세션 완전 종료

## 📁 프로젝트 구조

```
src/main/java/back/vybz/auth_user/
├── common/                    # 공통 모듈
│   ├── application/          # 공통 서비스
│   │   ├── ReissueService.java
│   │   └── TokenService.java
│   ├── config/               # 설정 클래스들
│   │   ├── ApplicationConfig.java
│   │   ├── RedisConfig.java
│   │   ├── RestTemplateConfig.java
│   │   ├── SecurityConfig.java
│   │   └── SwaggerConfig.java
│   ├── entity/               # 공통 엔티티
│   │   ├── BaseEntity.java
│   │   ├── BaseResponseEntity.java
│   │   ├── BaseResponseStatus.java
│   │   └── SoftDeletableEntity.java
│   ├── exception/            # 예외 처리
│   │   ├── AsyncExceptionHandler.java
│   │   ├── BaseException.java
│   │   ├── BaseExceptionHandler.java
│   │   └── BaseExceptionHandlerFilter.java
│   ├── jwt/                  # JWT 관련
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtProvider.java
│   └── util/                 # 유틸리티
│       └── RedisUtil.java
├── kafka/                    # Kafka 이벤트 처리
│   ├── config/               # Kafka 설정
│   │   └── UserAuthKafkaConfig.java
│   ├── event/                # 이벤트 모델
│   │   └── UserAuthEvent.java
│   └── producer/             # 이벤트 프로듀서
│       └── UserKafkaProducer.java
└── user/                     # 사용자 도메인
    ├── application/          # 사용자 서비스 로직
    │   ├── OAuthService.java
    │   └── OAuthServiceImpl.java
    ├── domain/               # 사용자 도메인 모델
    │   ├── CustomUserDetails.java
    │   ├── SocialType.java
    │   ├── Status.java
    │   └── User.java
    ├── dto/                  # 사용자 DTO
    │   ├── request/
    │   │   └── RequestOAuthSignInDto.java
    │   └── response/
    │       └── ResponseUserSignInDto.java
    ├── infrastructure/       # 사용자 리포지토리
    │   └── OAuthRepository.java
    ├── presentation/         # 사용자 컨트롤러
    │   └── OAuthController.java
    └── vo/                   # 사용자 VO
        ├── request/
        │   └── RequestUserSignInVo.java
        └── response/
            └── ResponseUserSignInVo.java
```

## 📚 API 문서

Swagger UI를 통해 API 문서를 확인할 수 있습니다:

-   **URL**: `http://localhost:8000/user-auth-service/swagger-ui/index.html`
-   **API 그룹**: OAUTH-SERVICE

### 주요 API 엔드포인트

#### OAuth 인증 API

-   `POST /api/v1/oauth/sign-in` - 소셜 로그인
-   `POST /api/v1/oauth/reissue` - Access Token 재발급
-   `POST /api/v1/oauth/sign-out` - 로그아웃

### API 요청/응답 예시

#### 소셜 로그인 요청

```json
{
    "provider": "GOOGLE",
    "providerId": "123456789",
    "email": "user@example.com",
    "nickname": "사용자명",
    "profileImageUrl": "https://example.com/profile.jpg"
}
```

#### 소셜 로그인 응답

```json
{
    "status": "SUCCESS",
    "message": "요청이 성공적으로 처리되었습니다.",
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "userUuid": "550e8400-e29b-41d4-a716-446655440000"
    }
}
```

## 🚀 설치 및 실행

### 1. 사전 요구사항

-   Java 17
-   Gradle 8.4+
-   Docker (선택사항)
-   MySQL 8.0
-   Redis
-   Kafka

### 2. 로컬 실행

```bash
# 프로젝트 클론
git clone <repository-url>
cd auth-user

# Gradle 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

### 3. Docker 실행

```bash
# Docker 이미지 빌드
docker build -t vybz-auth-user .

# Docker 컨테이너 실행
docker run -p 8000:8000 vybz-auth-user
```

## ⚙️ 환경 설정

### 주요 설정 파일

-   `application.yml`: 기본 설정
-   `application-dev.yml`: 개발 환경 설정
-   `application-db.yml`: 데이터베이스 설정

### 환경 변수

```yaml
# 데이터베이스 설정
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

# Redis 설정
spring:
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}

# Kafka 설정
spring:
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS}

# JWT 설정
JWT:
  secret-key: ${JWT_SECRET_KEY}
  token:
    access-expire-time: ${JWT_ACCESS_EXPIRE_TIME}
    refresh-expire-time: ${JWT_REFRESH_EXPIRE_TIME}

# OAuth 설정
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
          kakao:
            client-id: ${KAKAO_CLIENT_ID}
            client-secret: ${KAKAO_CLIENT_SECRET}
```

## 🔐 인증 시스템

### JWT 토큰 구조

#### Access Token

```json
{
    "user_uuid": "550e8400-e29b-41d4-a716-446655440000",
    "token_type": "access",
    "iat": 1640995200,
    "exp": 1640997000
}
```

#### Refresh Token

```json
{
    "sub": "550e8400-e29b-41d4-a716-446655440000",
    "token_type": "refresh",
    "iat": 1640995200,
    "exp": 1642125600
}
```

### 인증 플로우

1. **소셜 로그인**: 사용자가 Google/Kakao로 로그인
2. **사용자 검증**: Provider ID와 이메일로 기존 사용자 확인
3. **토큰 발급**: Access Token, Refresh Token 생성
4. **Redis 저장**: Refresh Token을 Redis에 저장 (15일)
5. **API 요청**: Access Token으로 API 호출
6. **토큰 검증**: JWT 필터에서 토큰 유효성 검증
7. **토큰 재발급**: Refresh Token으로 Access Token 재발급

### 보안 설정

-   **CORS**: 모든 오리진 허용 (개발 환경)
-   **CSRF**: 비활성화 (JWT 기반 인증)
-   **세션**: Stateless 세션 관리
-   **헤더**: Authorization 헤더를 통한 토큰 전달

## 📡 이벤트 처리

### Kafka 이벤트

#### 발행 이벤트

-   **UserAuthEvent**: 사용자 생성 이벤트
    -   `userUuid`: 사용자 UUID
    -   `nickname`: 사용자 닉네임
    -   `profileImageUrl`: 프로필 이미지 URL

### 이벤트 프로듀서

-   `UserKafkaProducer`: 사용자 생성 이벤트 발행

### Kafka 토픽

-   `create-user-auth`: 사용자 생성 이벤트 토픽

### 이벤트 발행 시점

-   **신규 사용자 등록**: OAuth 로그인 시 신규 사용자 생성 후
-   **이벤트 데이터**: 사용자 UUID, 닉네임, 프로필 이미지 URL

## 🏗 아키텍처

### 도메인 주도 설계 (DDD)

-   **Domain Layer**: 사용자 도메인 모델과 비즈니스 로직
-   **Application Layer**: OAuth 서비스 로직과 유스케이스
-   **Infrastructure Layer**: 데이터베이스 접근과 외부 시스템 연동
-   **Presentation Layer**: REST API 엔드포인트

### 마이크로서비스 패턴

-   **Service Discovery**: Eureka Client를 통한 서비스 등록
-   **Event-Driven**: Kafka를 통한 비동기 이벤트 처리
-   **Stateless**: 상태 없는 서비스 설계

### 데이터베이스 설계

-   **MySQL**: 사용자 정보, 소셜 로그인 데이터
-   **Redis**: Refresh Token 저장 및 세션 관리

## 🔧 개발 가이드

### 코드 컨벤션

-   **패키지 구조**: 도메인별 계층 분리
-   **네이밍**: 명확하고 일관된 네이밍 규칙
-   **예외 처리**: BaseException을 통한 통일된 예외 처리
-   **로깅**: Slf4j를 통한 구조화된 로깅

### 테스트

```bash
# 단위 테스트 실행
./gradlew test

# 통합 테스트 실행
./gradlew integrationTest
```

### OAuth 설정

#### Google OAuth

1. Google Cloud Console에서 프로젝트 생성
2. OAuth 2.0 클라이언트 ID 생성
3. 승인된 리디렉션 URI 설정
4. Client ID, Client Secret 환경 변수 설정

#### Kakao OAuth

1. Kakao Developers에서 애플리케이션 생성
2. 플랫폼 설정 (Web 플랫폼 추가)
3. 카카오 로그인 활성화
4. Client ID, Client Secret 환경 변수 설정

## 📝 라이선스

이 프로젝트는 VYBZ 팀의 내부 프로젝트입니다.

## 👥 팀

-   **개발팀**: VYBZ Backend Team

---

**VYBZ Auth User Service** - 안전하고 편리한 소셜 로그인 서비스
