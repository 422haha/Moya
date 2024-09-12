FROM openjdk:17-jdk-alpine
WORKDIR /app
VOLUME /tmp
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# 빌드 결과물 (JAR) 복사
COPY target/moya-0.0.1-SNAPSHOT.jar app.jar

# 환경 변수 파일 복사
COPY .env .env

# 설정 파일 복사
COPY backend/src/main/resources/application.properties application.properties
COPY backend/src/main/resources/application-oauth-kakao.properties application-oauth-kakao.properties

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]