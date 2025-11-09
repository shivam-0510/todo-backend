# Multi-stage build for API Gateway
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml /app/pom.xml

# Copy all module poms for dependency resolution
COPY discovery-server/pom.xml /app/discovery-server/pom.xml
COPY apigateway/pom.xml /app/apigateway/pom.xml
COPY apigateway/src /app/apigateway/src

# Build the application
WORKDIR /app/apigateway
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the JAR file
COPY --from=build /app/apigateway/target/apigateway-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

