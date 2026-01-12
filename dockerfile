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

# Create non-root user with specific UID/GID
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring

# Create entrypoint script that runs as root to setup permissions
RUN echo '#!/bin/sh' > /docker-entrypoint.sh && \
    echo 'set -e' >> /docker-entrypoint.sh && \
    echo '' >> /docker-entrypoint.sh && \
    echo '# Create log directories and set proper ownership' >> /docker-entrypoint.sh && \
    echo 'mkdir -p /app/logs/audit' >> /docker-entrypoint.sh && \
    echo 'chown -R spring:spring /app/logs' >> /docker-entrypoint.sh && \
    echo 'chmod -R 755 /app/logs' >> /docker-entrypoint.sh && \
    echo '' >> /docker-entrypoint.sh && \
    echo '# Switch to spring user and start application' >> /docker-entrypoint.sh && \
    echo 'exec su spring -c "java -jar /app/app.jar"' >> /docker-entrypoint.sh && \
    chmod +x /docker-entrypoint.sh

# Expose port
EXPOSE 8080

# Health check (note: runs as root in this setup)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Entry point (will run as root to setup permissions, then switch to spring user)
ENTRYPOINT ["/docker-entrypoint.sh"]
