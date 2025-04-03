# Use OpenJDK 17 (or change to 8/11 if needed)
FROM openjdk:17-jdk

# Copy the built JAR file from build/libs directory
ADD build/libs/employee-mgmnt-0.0.1-SNAPSHOT.jar employee-mgmnt-0.0.1-SNAPSHOT.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java" ,"-jar", "employee-mgmnt-0.0.1-SNAPSHOT.jar"]
