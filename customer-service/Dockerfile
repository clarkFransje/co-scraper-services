# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Build the application using Maven
RUN mvn package -e

# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/customer.jar .

# Expose the port your application listens on
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "customer.jar"]
