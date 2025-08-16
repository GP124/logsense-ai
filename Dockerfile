FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the generated jar (with wildcard to handle version names)
COPY target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
