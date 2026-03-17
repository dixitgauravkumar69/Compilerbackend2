# ---------- STAGE 1 : BUILD ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -e -C dependency:go-offline

COPY src ./src
RUN mvn -T 1C clean package -DskipTests

# ---------- STAGE 2 : RUN ----------
# JDK use karna zaroori hai agar javac (Java compilation) backend se karni hai
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Compilers aur Tools install karein
RUN apt-get update && apt-get install -y --no-install-recommends \
    g++ \
    gcc \
    python3 \
    && rm -rf /var/lib/apt/lists/*

# App jar copy karein
COPY --from=build /app/target/*.jar app.jar

# Render par tmp folder already hota hai, but permissions check ke liye
RUN chmod 777 /tmp

EXPOSE 8080

# Performance flags ke saath run karein
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]