# spring-boot-itba
Challenge - ITBA


## Proyecto de Gestión Académica

Este proyecto es una aplicación Spring Boot que ofrece una API RESTful para gestionar Alumnos, Materias, Aulas y Cursos. Utiliza una arquitectura en capas, JPA/Hibernate con H2 (base de datos en memoria) para persistencia. Incluye validaciones de negocio, procesamiento asíncrono (al eliminar aulas), Dockerización para fácil despliegue y pruebas unitarias para asegurar la calidad del código.

Cómo Compilar y Ejecutar:

Requisitos Previos:

- Java 17+
- Maven 3.6.0+
- Docker Desktop

Opción 1: Ejecución Local (con Maven)

Clonar: git clone <URL_DE_TU_REPOSITORIO> y cd spring-boot-itba.
Compilar: mvn clean install
Ejecutar: java -jar target/spring-boot-itba-0.0.1-SNAPSHOT.jar (ajusta el nombre del JAR). La app estará en http://localhost:8080.

Opción 2: Ejecución Vía Docker

Clonar: git clone <URL_DE_TU_REPOSITORIO> y cd spring-boot-itba.
Construir imagen: docker-compose build
Levantar: docker-compose up La app estará en http://localhost:8080.
Detener: Ctrl+C y luego docker-compose down para limpiar contenedores.

Ejemplos de Endpoints (cURL)
La aplicación se ejecuta en http://localhost:8080.

Alumnos (/api/alumnos)

Crear (POST):


```Bash
curl -X POST http://localhost:8080/api/alumnos \
  -H 'Content-Type: application/json' \
  -d '{ "nombre": "Federico", "apellido": "Gomez", "dni": 12345678, "matricula": 2023001, "direccion": "Av. Libertador 100", "edad": 28 }'
Respuesta: HTTP 201 Created
```
Listar Todos (GET):

```Bash
curl http://localhost:8080/api/alumnos
Respuesta: HTTP 200 OK
```
Obtener por ID (GET):

```Bash
curl http://localhost:8080/api/alumnos/1
Respuesta: HTTP 200 OK (o 404 Not Found)
```
Actualizar (PUT):

```Bash
curl -X PUT http://localhost:8080/api/alumnos/1 \
  -H 'Content-Type: application/json' \
  -d '{ "nombre": "Federico Actualizado", "apellido": "Gomez", "dni": 12345678, "matricula": 2023001, "direccion": "Nueva Direccion 456", "edad": 29 }'
Respuesta: HTTP 200 OK (o 404 Not Found / 409 Conflict)
```
Eliminar (DELETE):

```Bash
curl -X DELETE http://localhost:8080/api/alumnos/1
Respuesta: HTTP 204 No Content (o 404 Not Found)
```
Materias (/api/materias)

Crear (POST):

```Bash
curl -X POST http://localhost:8080/api/materias \
  -H 'Content-Type: application/json' \
  -d '{ "nombre": "Programacion Avanzada", "creditos": 6, "carrera": "Ingenieria en Sistemas", "detalle": "Conceptos de estructuras de datos y algoritmos.", "programa": "Introduccion, Arrays, Listas, Arboles, Grafos, Complejidad" }'
```
Listar Todas (GET):

```Bash
curl http://localhost:8080/api/materias
Aulas (/api/aulas)
```
Crear (POST):

```Bash
curl -X POST http://localhost:8080/api/aulas \
  -H 'Content-Type: application/json' \
  -d '{ "numero": "A201", "capacidad": 40, "ubicacion": "Edificio Norte", "observaciones": "Equipada con pizarra digital y proyector." }'
```
Listar Todas (GET):

```Bash
curl http://localhost:8080/api/aulas
```
Eliminar (DELETE):

```Bash
curl -X DELETE http://localhost:8080/api/aulas/1
```
Nota: Desvincula cursos asociados. Respuesta: HTTP 204 No Content.
Cursos (/api/cursos)
Crear (POST): (Requiere id de una Materia y opcionalmente un id de Aula existentes)

```Bash

curl -X POST http://localhost:8080/api/cursos \
  -H 'Content-Type: application/json' \
  -d '{ "nombre": "Taller de Spring Boot", "descripcion": "Desarrollo de APIs REST.", "anio": 2025, "materia": { "id": 1 }, "aula": { "id": 1 } }'
```
Listar Todos (GET):

```Bash
curl http://localhost:8080/api/cursos
```
Consola H2
Para inspeccionar la base de datos en memoria (solo en ejecución local):

URL: http://localhost:8080/h2-console

Credenciales: 
JDBC URL: jdbc:h2:mem:testdb
- User Name: sa
- Password: 1234


