# Use an official OpenJDK runtime as the base image
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app
RUN apt-get update && apt-get install -y maven
# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project
COPY . .

# Package the application (builds the JAR file)
RUN mvn clean package -DskipTests

# Expose the port that your Spring Boot application runs on (default is 8080)
EXPOSE 9096

# Run the application
CMD ["java", "-jar", "target/em-project-0.0.1-SNAPSHOT.jar"]
