# ---------- STAGE 1 : BUILD ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -e -C dependency:go-offline

COPY src ./src

RUN mvn -T 1C clean package -DskipTests


# ---------- STAGE 2 : RUN ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    g++ \
    python3 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

RUN mkdir codes

EXPOSE 8080

ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]