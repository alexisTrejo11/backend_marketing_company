# Build stage
FROM gradle:8.11-jdk23 AS build
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle/wrapper gradle/wrapper
COPY build.gradle .
COPY settings.gradle .

# Download dependencies
RUN gradle dependencies --no-daemon

# Copy source code
COPY src src

# Build application
RUN gradle bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

# Install curl for health check
RUN apk add --no-cache curl

# Copy built jar
COPY --from=build /app/build/libs/*.jar app.jar

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Create logs directory with proper permissions
RUN mkdir -p /app/logs/audit && \
    chown -R spring:spring /app/logs

USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Entry point
ENTRYPOINT ["java", "-jar", "/app/app.jar"]