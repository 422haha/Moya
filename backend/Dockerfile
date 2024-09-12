FROM openjdk:17-jdk-alpine
WORKDIR /app
VOLUME /tmp
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
COPY target/moya-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env
COPY application.properties application.properties
COPY application-oauth-kakao.properties application-oauth-kakao.properties
ENTRYPOINT ["java", "-jar", "app.jar"]