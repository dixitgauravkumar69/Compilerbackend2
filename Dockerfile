# ---------- STAGE 1 : BUILD ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first to leverage Docker layer caching
COPY pom.xml .

# Use BuildKit cache mount for Maven dependencies
# This significantly speeds up builds on Render's build servers
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -e dependency:go-offline

COPY src ./src

# Use cache mount again for the build process
RUN --mount=type=cache,target=/root/.m2 \
    mvn -T 1C clean package -DskipTests

# ---------- STAGE 2 : RUN ----------
# Using 'jre' instead of 'jdk' if you don't need javac at runtime saves ~150MB
# Since you ARE building a compiler platform, we stick to JDK.
FROM eclipse-temurin:17-jdk-jammy

# Set environment variables for Render
ENV PORT=8080
WORKDIR /app

# Install only essential compilers.
# Added 'python3-minimal' to save space over the full python3 package.
RUN apt-get update && apt-get install -y --no-install-recommends \
    g++ \
    gcc \
    python3-minimal \
    && rm -rf /var/lib/apt/lists/*

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Create a non-root user (Security Best Practice)
RUN useradd -m runner
USER runner

EXPOSE 8080

# Optimization for 512MB RAM:
# 1. Use SerialGC to save memory overhead (better for small containers).
# 2. Xmx limit to ensure the OS and compilers have room to breathe.
ENTRYPOINT ["java", \
            "-XX:+UseSerialGC", \
            "-Xmx300m", \
            "-XX:+UseContainerSupport", \
            "-jar", "app.jar"]