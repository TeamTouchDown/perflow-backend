# 빌드 단계: Gradle을 사용해 JAR 파일을 생성
FROM eclipse-temurin:17-jdk-alpine AS build

# 필요한 패키지 설치
RUN apk add --no-cache bash git

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 파일 및 설정 파일 복사
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle/wrapper gradle/wrapper

# Gradle Wrapper 실행 권한 부여 및 의존성 다운로드
RUN chmod +x gradlew && ./gradlew build --no-daemon || return 0

# 전체 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew clean build --no-daemon

# 실행 단계: 실제 애플리케이션을 실행할 경량 이미지
FROM eclipse-temurin:17-jdk-alpine

# (옵션) 시간대 설정을 위해 tzdata 설치 및 설정
ENV TZ=Asia/Seoul

RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

# 비루트 사용자 생성 및 권한 설정 (보안 강화)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# 애플리케이션 JAR 파일 복사
COPY --from=build /app/build/libs/perflow-backend-0.0.1-SNAPSHOT.jar /app/app.jar

# 컨테이너 시작 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# (옵션) 애플리케이션 포트 노출
EXPOSE 8080
