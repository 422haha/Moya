# 빌드 단계
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# 실행 단계
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/target/moya-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]