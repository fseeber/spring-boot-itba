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

Clonar: git clone https://github.com/fseeber/spring-boot-itba.git y cd spring-boot-itba.
Compilar: mvn clean install
Ejecutar: java -jar target/spring-boot-itba-0.0.1-SNAPSHOT.jar (ajusta el nombre del JAR). La app estará en http://localhost:8080

Opción 2: Ejecución Vía Docker

Clonar: git clone https://github.com/fseeber/spring-boot-itba.git y cd spring-boot-itba.
Construir imagen: docker-compose build
Levantar: docker-compose up La app estará en http://localhost:8080.
Detener: Ctrl+C y luego docker-compose down para limpiar contenedores.

Endpoints
La aplicación se ejecuta en http://localhost:8080/api

### Alumnos

| Método | Endpoint | Descripción | Body Ejemplo (POST) |
| :----- | :-------------------------- | :----------------------------------------------- | :------------------------------------------------------------------- |
| `GET`  | `/alumnos`                  | Obtiene todos los alumnos.                       | N/A                                                                  |
| `GET`  | `/alumnos/{id}`             | Obtiene un alumno por ID.                        | N/A                                                                  |
| `POST` | `/alumnos`                  | Crea un nuevo alumno.                            | `{"nombre": "Federico", "apellido": "Seeber", "dni": 2342422, "matricula": 123123, "email": "fseeber@example.com", "direccion": "Calle Falsa 123", "edad": 37}` |
| `POST` | `/alumnos/{alumnoId}/inscribirCurso/{cursoId}` | Inscribe un alumno en un curso.                  | N/A                                                                  |
| `DELETE`| `/alumnos/{id}`             | Elimina un alumno por ID.     

### Aulas

| Método | Endpoint | Descripción | Body Ejemplo (POST) |
| :----- | :-------------------------- | :----------------------------------------------- | :------------------------------------------------------------------- |
| `GET`  | `/aulas` | Obtiene todas las aulas. | N/A |
| `GET`  | `/aulas/{id}` | Obtiene un aula por ID. | N/A |
| `POST` | `/aulas` | Crea una nueva aula. | `{"numero": "B2061", "capacidad": 50, "ubicacion": "Edificio Central2", "observaciones": "aula auxiliar"}` |
| `DELETE`| `/aulas/{id}` | Elimina un aula por ID. | N/A |

### Cursos

| Método | Endpoint | Descripción | Body Ejemplo (POST) |
| :----- | :-------------------------- | :----------------------------------------------- | :------------------------------------------------------------------- |
| `GET`  | `/cursos` | Obtiene todos los cursos. | N/A |
| `GET`  | `/cursos/{id}` | Obtiene un curso por ID. | N/A |
| `GET`  | `/cursos/con-alumnos` | Obtiene cursos con alumnos inscritos. | N/A |
| `POST` | `/cursos` | Crea un nuevo curso. | `{"nombre": "Nuevo Curso", "descripcion": "Descripción del curso.", "anio": 2026, "materia": { "id": 1 }, "aula": { "id": 1 }}` |
| `DELETE`| `/cursos/{id}` | Elimina un curso por ID. | N/A |

### Materias

| Método | Endpoint | Descripción | Body Ejemplo (POST) |
| :----- | :-------------------------- | :----------------------------------------------- | :------------------------------------------------------------------- |
| `GET`  | `/materias` | Obtiene todas las materias. | N/A |
| `GET`  | `/materias/{id}` | Obtiene una materia por ID. | N/A |
| `POST` | `/materias` | Crea una nueva materia. | `{"nombre": "Introduccion a Programacion", "creditos": 6, "carrera": "Ingenieria en Sistemas", "detalle": "Conceptos basicos", "programa": "Python y Java."}` |
| `DELETE`| `/materias/{id}` | Elimina una materia por ID. | N/A |

Consola H2
Para inspeccionar la base de datos en memoria (solo en ejecución local):

URL: http://localhost:8080/h2-console


Credenciales: 
JDBC URL: jdbc:h2:mem:testdb
- User Name: sa
- Password: 1234

### TODO:
- Implementar Swagger para documentar las API´s desarrolladas.
- Consumir los mensajes generados por RabbitMQ y leerlos desde una API nueva /getUltimosCursos disponibles.
- Implementar manejo global de excepciones.
- Mejorar la cobertura de los test en el resto de la aplicación.
- Incorporar algun reporte por ejemplo Jasper para obtener un listado de Alumnos, Cursos, Materias, etc. 
