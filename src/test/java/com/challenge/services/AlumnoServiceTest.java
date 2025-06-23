package com.challenge.services;

import com.challenge.dtos.AlumnoDto;
import com.challenge.entities.Alumno;
import com.challenge.repositories.AlumnoRepository;
import com.challenge.mappers.AlumnoMapper;

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

    @Mock 
    private AlumnoMapper alumnoMapper;

    @InjectMocks
    private AlumnoService alumnoService;

    private Alumno alumnoEntityWithId;
    private Alumno alumnoEntityWithoutId;
    private AlumnoDto alumnoDtoWithId;
    private AlumnoDto alumnoDtoForCreation;

    @BeforeEach
    void setUp() {
        alumnoDtoForCreation = new AlumnoDto(null, "Carlos", "Lopez", 11223344, null, 200, "San Martin 500", 25);

        alumnoEntityWithoutId = new Alumno(null, "Carlos", "Lopez", 11223344, 200, "San Martin 500", null, 25, null);

        alumnoEntityWithId = new Alumno(1L, "Carlos", "Lopez", 11223344, 200, "San Martin 500", null, 25, null);

        alumnoDtoWithId = new AlumnoDto(1L, "Carlos", "Lopez", 11223344, null, 200, "San Martin 500", 25);
    }

    @Test
    @DisplayName("Debería crear un alumno exitosamente")
    void shouldCreateAlumnoSuccessfully() {
        when(alumnoRepository.findByDni(alumnoDtoForCreation.getDni())).thenReturn(Optional.empty());

        when(alumnoMapper.toEntity(alumnoDtoForCreation)).thenReturn(alumnoEntityWithoutId);

        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoEntityWithId);

        when(alumnoMapper.toDto(alumnoEntityWithId)).thenReturn(alumnoDtoWithId);

        AlumnoDto createdAlumnoDto = alumnoService.guardarAlumno(alumnoDtoForCreation);

        assertThat(createdAlumnoDto).isNotNull();
        assertThat(createdAlumnoDto.getId()).isEqualTo(alumnoDtoWithId.getId());
        assertThat(createdAlumnoDto.getNombre()).isEqualTo(alumnoDtoWithId.getNombre());
        assertThat(createdAlumnoDto.getDni()).isEqualTo(alumnoDtoWithId.getDni());
        assertThat(createdAlumnoDto.getMatricula()).isEqualTo(alumnoDtoWithId.getMatricula());

        verify(alumnoRepository, times(1)).findByDni(alumnoDtoForCreation.getDni());
        verify(alumnoMapper, times(1)).toEntity(alumnoDtoForCreation);
        verify(alumnoRepository, times(1)).save(alumnoEntityWithoutId);
        verify(alumnoMapper, times(1)).toDto(alumnoEntityWithId);
    }

    @Test
    @DisplayName("Debería obtener todos los alumnos")
    void shouldGetAllAlumnos() {
        Alumno otroAlumnoEntity = new Alumno(2L, "Ana", "Ruiz", 55667788, 201, "seeberfederico@gmail.com","Los Andes 100", 28, null);
        AlumnoDto otroAlumnoDto = new AlumnoDto(2L, "Ana", "Ruiz", 55667788, null, 201, "Los Andes 100", 28);

        List<Alumno> alumnosEntities = Arrays.asList(alumnoEntityWithId, otroAlumnoEntity);
        List<AlumnoDto> alumnosDtos = Arrays.asList(alumnoDtoWithId, otroAlumnoDto);

        when(alumnoRepository.findAll()).thenReturn(alumnosEntities);
        when(alumnoMapper.toDtoList(alumnosEntities)).thenReturn(alumnosDtos);

        List<AlumnoDto> foundAlumnos = alumnoService.findAllAlumnos();

        assertThat(foundAlumnos).isNotNull();
        assertThat(foundAlumnos).hasSize(2);
        assertThat(foundAlumnos.get(0).getNombre()).isEqualTo("Carlos");
        assertThat(foundAlumnos.get(0).getDni()).isEqualTo(11223344);
        assertThat(foundAlumnos.get(1).getNombre()).isEqualTo("Ana");
        assertThat(foundAlumnos.get(1).getDni()).isEqualTo(55667788);

        verify(alumnoRepository, times(1)).findAll();
        verify(alumnoMapper, times(1)).toDtoList(alumnosEntities);
    }

}