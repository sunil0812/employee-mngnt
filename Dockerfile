# Use OpenJDK 17 (or change to 8/11 if needed)
FROM openjdk:17-jdk

# Explicitly create the /app directory before setting WORKDIR


# Copy the built JAR file from build/libs directory
ADD src/build/libs/employee-mgmnt-0.01-SNAPSHOT.jar employee-management.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar", "employee-management.jar"]
