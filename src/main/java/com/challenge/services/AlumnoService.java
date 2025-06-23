package com.challenge.services;

import com.challenge.dtos.AlumnoDto;
import com.challenge.entities.Alumno;
import com.challenge.mappers.AlumnoMapper;
import com.challenge.repositories.AlumnoRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;

    public AlumnoService(AlumnoRepository alumnoRepository, AlumnoMapper alumnoMapper) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoMapper = alumnoMapper;
    }

    /**
     * Guarda un nuevo alumno en la base de datos.
     * Recibe un AlumnoDto, lo convierte a entidad, lo guarda y devuelve el AlumnoDto resultante.
     * @param alumnoDto El DTO del alumno a guardar.
     * @return El DTO del alumno guardado (con el ID generado).
     */
    @Transactional
    public AlumnoDto guardarAlumno(AlumnoDto alumnoDto) {
        if (alumnoRepository.findByDni(alumnoDto.getDni()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI " + alumnoDto.getDni() + " ya está registrado para otro alumno.");
        }
        
        Alumno alumno = alumnoMapper.toEntity(alumnoDto);

        Alumno alumnoGuardado = alumnoRepository.save(alumno);

        AlumnoDto responseDto = alumnoMapper.toDto(alumnoGuardado);

        return responseDto;
    }

    /**
     * Obtiene una lista de todos los alumnos disponibles.
     *
     * @return Una lista de AlumnoDTO.
     */
    public List<AlumnoDto> findAllAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        return alumnoMapper.toDtoList(alumnos);
    }
    
    /**
     * Obtiene un alumno segun su ID
     *
     * @param id id del alumno que recibe
     * @return retorna un alumno en caso de existir
     */
    public Optional<AlumnoDto> findAlumnoById(Long id) {
        Optional<Alumno> alumnoEntity = alumnoRepository.findById(id);
        return alumnoEntity.map(alumnoMapper::toDto); 
    }
    
    /**
     * Elimina un alumno por su ID.
     * @param id El ID del alumno a eliminar.
     * @throws ResponseStatusException Si el alumno no se encuentra.
     */
    @Transactional
    public void eliminarAlumno(Long id) {
        if (!alumnoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno con ID " + id + " no encontrado para eliminar.");
        }
        alumnoRepository.deleteById(id);
    }
}
