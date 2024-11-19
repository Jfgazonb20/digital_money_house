# Usar una imagen base de Java
FROM openjdk:17-jdk-slim

# Información de los autores
LABEL authors="jfgaz"

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

COPY target/digital-money-house-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que utiliza la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
