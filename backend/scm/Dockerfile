# Stage 1: Build the application
FROM maven:3.8.5-openjdk-21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files to the container
COPY . .

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:21

LABEL authors="MMA" \
      description="This is a simple Spring Boot application for managing a list of contacts"

# Set the working directory
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Specify the command to run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
