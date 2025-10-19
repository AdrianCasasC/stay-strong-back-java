# Etapa 1: Build con Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos del proyecto Maven (pom.xml y código fuente)
COPY pom.xml .
COPY src ./src

# Compila el proyecto y genera el .jar (sin tests opcionalmente)
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para ejecutar el .jar
FROM eclipse-temurin:21-jdk

# Directorio de trabajo en la imagen final
WORKDIR /app

# Copia el .jar generado desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Puerto que expone la aplicación (ajústalo si es necesario)
EXPOSE 8080

# Comando para ejecutar el .jar
ENTRYPOINT ["java", "-jar", "app.jar"]
