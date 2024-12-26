FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/em_project.jar em_project.jar
EXPOSE 9095
ENTRYPOINT ["java", "-jar", "em_project.jar"]
