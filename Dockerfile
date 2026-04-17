# syntax=docker/dockerfile:1.7

# ---------- STAGE 1 : BUILD ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven descriptor first for better caching
COPY pom.xml .

# Pre-fetch dependencies
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -e dependency:go-offline

# Copy source after dependencies
COPY src ./src

# Build using same cache mount pattern
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C clean package -DskipTests

# ---------- STAGE 2 : RUNTIME ----------
FROM eclipse-temurin:17-jre-jammy

ENV PORT=8080
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    g++ \
    python3-minimal \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar /app/app.jar

RUN useradd -m -u 10001 runner
USER runner

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseSerialGC", "-XX:+UseContainerSupport", "-Xmx300m", "-jar", "/app/app.jar"]