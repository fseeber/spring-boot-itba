package com.challenge.services;

import com.challenge.dtos.AlumnoDto;
import com.challenge.entities.Alumno;
import com.challenge.repositories.AlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para AlumnoService")
class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoService alumnoService;

    private Alumno alumno;
    private AlumnoDto alumnoDto;

    @BeforeEach
    void setUp() {
        alumno = new Alumno(1L, "Carlos", "Lopez", 11223344, 200, "San Martin 500", 25);
        alumnoDto = new AlumnoDto(1L, "Carlos", "Lopez", 11223344, 200, "San Martin 500", 25);
    }

    @Test
    @DisplayName("Debería crear un alumno exitosamente")
    void shouldCreateAlumnoSuccessfully() {

        when(alumnoRepository.findByDni(alumnoDto.getDni())).thenReturn(Optional.empty());
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumno);


        AlumnoDto createdAlumnoDto = alumnoService.guardarAlumno(alumnoDto);

        assertThat(createdAlumnoDto).isNotNull();
        assertThat(createdAlumnoDto.getId()).isEqualTo(alumno.getId());
        assertThat(createdAlumnoDto.getNombre()).isEqualTo(alumno.getNombre());

        verify(alumnoRepository, times(1)).findByDni(alumnoDto.getDni());
        verify(alumnoRepository, times(1)).save(any(Alumno.class));
    }

    @Test
    @DisplayName("Debería obtener todos los alumnos")
    void shouldGetAllAlumnos() {
        Alumno otroAlumno = new Alumno(2L, "Ana", "Ruiz", 55667788, 201, "Los Andes 100", 28);
        List<Alumno> alumnos = Arrays.asList(alumno, otroAlumno);
        when(alumnoRepository.findAll()).thenReturn(alumnos);

        List<AlumnoDto> foundAlumnos = alumnoService.findAllAlumnos();

        assertThat(foundAlumnos).isNotNull();
        assertThat(foundAlumnos).hasSize(2);
        assertThat(foundAlumnos.get(0).getNombre()).isEqualTo("Carlos");
        assertThat(foundAlumnos.get(1).getNombre()).isEqualTo("Ana");
        verify(alumnoRepository, times(1)).findAll();
    }
}