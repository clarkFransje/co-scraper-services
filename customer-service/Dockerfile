# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /app/src/
RUN mvn package -DskipTests

# Stage 2: Create the final Docker image
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/customer-service-1.0-SNAPSHOT.jar customer-service.jar
EXPOSE 8080
CMD ["java", "-jar", "customer-service.jar"]
