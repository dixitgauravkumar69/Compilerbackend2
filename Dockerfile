# Stage 1: Build Stage (Isme Maven JAR generate karega)
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# 1. pom.xml copy karke dependencies download karein
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Source code copy karke JAR file banayein
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run Stage (Isme final app chalega)
FROM eclipse-temurin:17-jdk-jammy

# 3. C++ aur Python install karein (Aapke compiler ke liye zaroori hai)
RUN apt-get update && apt-get install -y \
    build-essential \
    python3 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 4. Pichle 'build' stage se JAR file copy karein
# Dhyan dein: '--from=build' likhna zaroori hai taaki wo Docker ke andar se hi JAR uthaye
COPY --from=build /app/target/*.jar app.jar

# 5. Code execution ke liye folder
RUN mkdir /app/codes

# Port expose (Render automatically 8080 detect kar leta hai)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]