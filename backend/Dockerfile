FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
COPY target/moya-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/application-oauth-kakao.properties application-oauth-kakao.properties
COPY src/main/resources/application-oauth-naver.properties application-oauth-naver.properties
ENTRYPOINT ["java", "-jar", "app.jar"]