# Base Java 8 (maintenue)
FROM eclipse-temurin:8-jdk AS build

# Installer Maven pour Java 8
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

WORKDIR /app

# Copier tout le projet
COPY . .

# Build du projet
RUN mvn -DskipTests clean package

# ----------- Runtime -----------

FROM eclipse-temurin:8-jdk

WORKDIR /app

# ⭐ Très important : Render doit connaître le port utilisé par Spring Boot
EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
