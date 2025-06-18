INSERT INTO ALUMNOS (nombre, apellido, dni, matricula, direccion, edad) VALUES
('Lionel', 'Messi', 34567890, 10001, 'Rosario', 37),
('Emiliano', 'Martinez', 40123456, 10002, 'Mar del Plata', 31),
('Julian', 'Alvarez', 45678901, 10003, 'Calchin', 24),
('Enzo', 'Fernandez', 47890123, 10004, 'San Martin', 23),
('Paulo', 'Dybala', 38901234, 10005, 'Laguna Larga', 30);

INSERT INTO MATERIAS (nombre, creditos, carrera, detalle, programa) VALUES
('Programacion Orientada a Objetos', 8, 'Ingenieria en Sistemas', 'Conceptos avanzados de POO y Java', 'Clases, objetos, herencia, polimorfismo, interfaces'),
('Bases de Datos I', 6, 'Licenciatura en Informatica', 'Introduccion a sistemas gestores de bases de datos', 'Modelado ER, SQL, normalizacion'),
('Estructuras de Datos', 7, 'Ingenieria en Sistemas', 'Implementacion de estructuras de datos fundamentales', 'Listas, arboles, grafos, algoritmos de busqueda y ordenacion'),
('Calculo Diferencial e Integral', 9, 'Ingenieria en Sistemas', 'Estudio de funciones, limites, derivadas e integrales', 'Aplicaciones en fisica y ciencias'),
('Gestion de Proyectos de Software', 5, 'Licenciatura en Informatica', 'Metodologias y herramientas para la gestion de proyectos', 'Scrum, Kanban, estimacion, riesgos');

INSERT INTO AULAS (id, numero, capacidad, ubicacion, observaciones) VALUES
(1, 'A101', 30, 'Edificio Central', 'Aula con proyector y pizarra interactiva'),
(2, 'B205', 20, 'Anexo Nuevo', 'Aula pequeña ideal para tutorias');

INSERT INTO CURSOS (nombre, descripcion, anio, materia_id, aula_id) VALUES
('Desarrollo Backend con Spring Boot', 'Curso avanzado de desarrollo de APIs RESTful', 2025, 1, 1),
('Introduccion a Bases de Datos', 'Primeros pasos con SQL y modelado de datos', 2024, 1, 2),
('Arquitectura de Software', 'Patrones y principios de diseño de sistemas', 2025, 1, 1);