# Use a base image with OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the entire project
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the application port (change if your app runs on a different port)
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "target/Service-Provider-System-0.0.1-SNAPSHOT.jar"]