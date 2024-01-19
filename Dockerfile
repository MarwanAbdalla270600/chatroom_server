# Use the official Gradle image with OpenJDK 17
FROM gradle:7.4.2-jdk17 AS build

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Gradle configuration files
COPY build.gradle.kts settings.gradle.kts ./

# Copy the source code
COPY src ./src

# Build the application
RUN gradle clean build --no-daemon

# Use OpenJDK 17 for running the application
FROM openjdk:17

# Set the working directory in the Docker image
WORKDIR /app

# Copy the built jar file from the build stage
COPY ./build/libs/chatroom_server-1.0-SNAPSHOT-all.jar ./app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]
