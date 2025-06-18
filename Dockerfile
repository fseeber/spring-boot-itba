# Usa una imagen base de OpenJDK para construir el proyecto.
# Esta etapa se llama 'builder'.
FROM openjdk:17-jdk-slim AS builder

# Establece el directorio de trabajo dentro del contenedor.
WORKDIR /app

# --- PASO NUEVO: INSTALAR MAVEN ---
# Instala las herramientas necesarias para la descarga (wget) y Maven.
# 'apt-get update' actualiza la lista de paquetes.
# 'apt-get install -y' instala los paquetes sin pedir confirmación.
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/* # Limpia el caché de apt para reducir el tamaño de la imagen

# Copia los archivos de configuración de Maven (pom.xml y .mvn)
# para permitir que Maven descargue dependencias.
COPY .mvn/ .mvn
COPY pom.xml .

# Descarga las dependencias de Maven. Esto aprovecha el caché de Docker:
# si el pom.xml no cambia, estas dependencias no se descargarán de nuevo.
RUN mvn dependency:go-offline

# Copia todo el código fuente de tu aplicación.
COPY src ./src

# Empaqueta la aplicación en un archivo JAR ejecutable.
# La bandera -DskipTests es opcional, pero común en Dockerfiles de producción.
RUN mvn clean package

# --- Segunda etapa: Crear la imagen final de la aplicación ---
# Usa una imagen base más pequeña para la ejecución final, lo que reduce el tamaño de la imagen.
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo para la aplicación.
WORKDIR /app

# Copia el JAR ejecutable de la etapa 'builder' al directorio de trabajo de la imagen final.
# La ruta 'target/*.jar' asume que el JAR se encuentra directamente en el directorio target.
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto en el que la aplicación Spring Boot se ejecutará (por defecto 8080).
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie.
# Usa 'java -jar' para ejecutar el JAR empaquetado.
ENTRYPOINT ["java", "-jar", "app.jar"]