FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN apk add --no-cache libgcc
COPY target/moya-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/application-oauth-kakao.properties application-oauth-kakao.properties
ENTRYPOINT ["java", "-jar", "app.jar"]