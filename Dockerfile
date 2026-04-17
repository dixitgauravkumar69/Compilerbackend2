# syntax=docker/dockerfile:1.7

# ---------- STAGE 1 : DEPENDENCIES ----------
FROM maven:3.9.9-eclipse-temurin-17 AS deps
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -e dependency:go-offline

# ---------- STAGE 2 : BUILD ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY --from=deps /root/.m2 /root/.m2
COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C clean package -DskipTests

# Extract Spring Boot layers for better caching
RUN java -Djarmode=layertools -jar /app/target/*.jar extract

# ---------- STAGE 3 : RUNTIME ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
ENV PORT=8080

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    g++ \
    python3-minimal \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN useradd -m -u 10001 runner

COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

USER runner
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseSerialGC", "-XX:+UseContainerSupport", "-Xmx300m", "org.springframework.boot.loader.launch.JarLauncher"]