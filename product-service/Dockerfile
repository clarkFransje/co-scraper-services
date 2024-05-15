# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn package

# Stage 2: Create the final Docker image
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/product-service-1.0-SNAPSHOT.jar product.jar
EXPOSE 8080
CMD ["java", "-jar", "product.jar"]
