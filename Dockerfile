# Use OpenJDK 17 (or change to 8/11 if needed)
FROM openjdk:17-jdk

# Explicitly create the /app directory before setting WORKDIR
RUN mkdir -p /app

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from build/libs directory
COPY --from= src /build/libs/*.jar /app/employee-management.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar", "/app/employee-management.jar"]
