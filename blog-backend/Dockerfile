# Backend Dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S blog && adduser -S blog -G blog

# Copy JAR file
COPY blog-bootstrap/target/blog-bootstrap-*.jar app.jar

# Set ownership
RUN chown -R blog:blog /app

# Switch to non-root user
USER blog

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]