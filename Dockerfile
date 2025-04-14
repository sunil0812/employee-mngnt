# Use OpenJDK 17 (or change to 8/11 if needed)
FROM openjdk:17-jdk

# Explicitly create the /app directory before setting WORKDIR
RUN mkdir -p /app

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from build/libs directory
COPY jar/employee-mgmnt-0.0.1.jar /app/employee-mgmnt-0.0.1.jar
# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar", "/app/employee-mgmnt-0.0.1.jar"]