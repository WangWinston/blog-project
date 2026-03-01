# Backend Dockerfile - Multi-stage build
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy pom files first for better caching
COPY pom.xml .
COPY blog-common/pom.xml blog-common/
COPY blog-domain/pom.xml blog-domain/
COPY blog-infrastructure/pom.xml blog-infrastructure/
COPY blog-application/pom.xml blog-application/
COPY blog-interfaces/pom.xml blog-interfaces/
COPY blog-bootstrap/pom.xml blog-bootstrap/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY blog-common/ blog-common/
COPY blog-domain/ blog-domain/
COPY blog-infrastructure/ blog-infrastructure/
COPY blog-application/ blog-application/
COPY blog-interfaces/ blog-interfaces/
COPY blog-bootstrap/ blog-bootstrap/

# Build application
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S blog && adduser -S blog -G blog

# Copy JAR from builder
COPY --from=builder /build/blog-bootstrap/target/blog-bootstrap-*.jar app.jar

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