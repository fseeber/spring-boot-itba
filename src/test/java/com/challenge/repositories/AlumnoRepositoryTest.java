package com.challenge.repositories;

import com.challenge.entities.Alumno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("Pruebas Unitarias para AlumnoRepository")
class AlumnoRepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Test
    @DisplayName("Debería guardar un alumno correctamente")
    void shouldSaveAlumno() {
        Alumno alumno = new Alumno(null, "Juan", "Perez", 12345678, 100, "seeberfederico@gmail.com", "Calle Falsa 123", 20, null);

        Alumno savedAlumno = alumnoRepository.save(alumno);

        assertThat(savedAlumno).isNotNull();
        assertThat(savedAlumno.getId()).isNotNull();
        assertThat(savedAlumno.getNombre()).isEqualTo("Juan");
    }

    @Test
    @DisplayName("Debería encontrar un alumno por DNI")
    void shouldFindAlumnoByDni() {
        Alumno alumno = new Alumno(null, "Maria", "Gomez", 87654321, 101, "Avenida Siempre Viva", 22);
        alumnoRepository.save(alumno); 

        Optional<Alumno> foundAlumno = alumnoRepository.findByDni(87654321);

        assertThat(foundAlumno).isPresent();
        assertThat(foundAlumno.get().getNombre()).isEqualTo("Maria");
        assertThat(foundAlumno.get().getDni()).isEqualTo(87654321);
    }

    @Test
    @DisplayName("No debería encontrar alumno por DNI si no existe")
    void shouldNotFindAlumnoByDniIfNotFound() {
        Optional<Alumno> foundAlumno = alumnoRepository.findByDni(99999999);

        assertThat(foundAlumno).isEmpty();
    }
}