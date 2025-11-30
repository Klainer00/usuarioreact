# --- ETAPA 1: BUILD (Compilación) ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copiamos primero el pom.xml para aprovechar la caché de capas de Docker
COPY pom.xml .
# Descargamos dependencias (esto hace que la build sea más rápida en el futuro)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el jar saltando los tests para agilizar
RUN mvn clean package -DskipTests

# --- ETAPA 2: RUN (Ejecución) ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR de la etapa de BUILD
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8180

ENV SPRING_DATASOURCE_URL=jdbc:mysql://34.176.27.161:3306/usuario
ENV SPRING_DATASOURCE_USERNAME=huerto-admin
ENV SPRING_DATASOURCE_PASSWORD=Zu0=lKu/`XU:DAtY

ENTRYPOINT ["java", "-jar", "app.jar"]