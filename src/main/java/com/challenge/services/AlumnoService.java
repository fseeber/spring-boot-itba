package com.challenge.services;

import com.challenge.dtos.AlumnoDto;
import com.challenge.entities.Alumno;
import com.challenge.mappers.AlumnoMapper;
import com.challenge.repositories.AlumnoRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlumnoService {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoService.class);

    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;

    public AlumnoService(AlumnoRepository alumnoRepository, AlumnoMapper alumnoMapper) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoMapper = alumnoMapper;
        logger.info("AlumnoService inicializado.");
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
            logger.warn("Intento de guardar alumno fallido: El DNI {} ya está registrado.", alumnoDto.getDni());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI " + alumnoDto.getDni() + " ya está registrado para otro alumno.");
        }
        
        Alumno alumno = alumnoMapper.toEntity(alumnoDto);
        Alumno alumnoGuardado = alumnoRepository.save(alumno);
        logger.info("Alumno guardado exitosamente con ID: {}", alumnoGuardado.getId());

        return alumnoMapper.toDto(alumnoGuardado);
    }

    /**
     * Obtiene una lista de todos los alumnos disponibles.
     *
     * @return Una lista de AlumnoDTO.
     */
    public List<AlumnoDto> findAllAlumnos() {
        logger.info("Buscando todos los alumnos.");
        List<Alumno> alumnos = alumnoRepository.findAll();
        logger.info("Se encontraron {} alumnos.", alumnos.size());
        return alumnoMapper.toDtoList(alumnos);
    }
    
    /**
     * Obtiene un alumno segun su ID
     *
     * @param id id del alumno que recibe
     * @return retorna un alumno en caso de existir
     */
    public Optional<AlumnoDto> findAlumnoById(Long id) {
        logger.info("Buscando alumno con ID: {}", id);
        Optional<Alumno> alumnoEntity = alumnoRepository.findById(id);
        if (alumnoEntity.isEmpty()) {
            logger.warn("Alumno con ID {} no encontrado.", id);
        }
        return alumnoEntity.map(alumnoMapper::toDto); 
    }
    
    /**
     * Elimina un alumno por su ID.
     * @param id El ID del alumno a eliminar.
     * @throws ResponseStatusException Si el alumno no se encuentra.
     */
    @Transactional
    public void eliminarAlumno(Long id) {
        logger.info("Intentando eliminar alumno con ID: {}", id);
        if (!alumnoRepository.existsById(id)) {
            logger.warn("Intento de eliminación fallido: Alumno con ID {} no encontrado.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno con ID " + id + " no encontrado para eliminar.");
        }
        alumnoRepository.deleteById(id);
        logger.info("Alumno con ID {} eliminado exitosamente.", id);
    }
}