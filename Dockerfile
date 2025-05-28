# Multi-stage build
FROM openjdk:17-jdk-slim AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper files
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copy pom.xml
COPY pom.xml .

# Make maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (for caching)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]