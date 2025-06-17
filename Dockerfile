# Use gradle image for build stage
FROM gradle:8.5.0-jdk17 AS build

# Copy project into Docker image
COPY --chown=gradle:gradle . /home/gradle/project

WORKDIR /home/gradle/project

# Run gradle build
RUN gradle build --no-daemon

# Now start second stage for slim runtime
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from build/libs directory
COPY --from=build /home/gradle/project/build/libs/employee-mgmnt-0.0.1.jar /app/employee-mgmnt-0.0.1.jar
# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar", "/app/employee-mgmnt-0.0.1.jar"]