FROM openjdk:17-jdk-alpine
WORKDIR /app
VOLUME /tmp
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# JAR 파일 복사
COPY target/moya-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]