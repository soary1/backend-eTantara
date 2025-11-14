# Étape 1 — Build avec Maven + Java 8
FROM maven:3.8.7-openjdk-8 AS build
WORKDIR /app
COPY . .
RUN mvn -DskipTests clean package

# Étape 2 — Exécuter l'application avec Java 8
FROM eclipse-temurin:8-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
