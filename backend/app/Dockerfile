# =========================
# 1) Build stage
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos pom.xml y el maven wrapper para cachear dependencias
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiamos el resto del proyecto
COPY src ./src

# Compilamos
RUN ./mvnw clean package -DskipTests

# =========================
# 2) Runtime stage
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Arranque del contenedor (con soporte para variables JVM)
ENTRYPOINT ["java", "-jar", "app.jar"]