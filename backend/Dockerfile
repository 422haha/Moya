FROM openjdk:17-jdk-alpine
VOLUME /tmp
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
COPY target/moya-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]