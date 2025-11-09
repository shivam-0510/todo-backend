# Multi-stage build for Discovery Server
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml /app/pom.xml

# Copy discovery-server pom and source
COPY discovery-server/pom.xml /app/discovery-server/pom.xml
COPY discovery-server/src /app/discovery-server/src

# Build the application
WORKDIR /app/discovery-server
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install wget for health checks
RUN apk add --no-cache wget

# Copy the JAR file
COPY --from=build /app/discovery-server/target/discovery-server-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

