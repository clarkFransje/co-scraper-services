# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /app/src/
RUN mvn package

# Stage 2: Create the final Docker image
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/store-service-1.0-SNAPSHOT.jar store-service.jar
EXPOSE 8083
CMD ["java", "-jar", "store-service.jar"]
