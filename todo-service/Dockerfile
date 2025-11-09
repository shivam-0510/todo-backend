# Multi-stage build for Todo Service
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml /app/pom.xml

# Copy all module poms for dependency resolution
COPY discovery-server/pom.xml /app/discovery-server/pom.xml
COPY apigateway/pom.xml /app/apigateway/pom.xml
COPY user-service/pom.xml /app/user-service/pom.xml
COPY todo-service/pom.xml /app/todo-service/pom.xml
COPY todo-service/src /app/todo-service/src

# Build the application
WORKDIR /app/todo-service
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR file
COPY --from=build /app/todo-service/target/todo-service-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

