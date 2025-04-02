FROM openjdk:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file from build/libs on your machine to /app inside the container
COPY build/libs/employee-mgmnt-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
