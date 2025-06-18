package com.challenge.services;

import com.challenge.dtos.AlumnoDto;
import com.challenge.entities.Alumno;
import com.challenge.repositories.AlumnoRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

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
        Alumno alumno = new Alumno();
        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setDni(alumnoDto.getDni());
        alumno.setMatricula(alumnoDto.getMatricula());
        alumno.setDireccion(alumnoDto.getDireccion());
        alumno.setEdad(alumnoDto.getEdad());

        Alumno alumnoGuardado = alumnoRepository.save(alumno);

        AlumnoDto responseDto = new AlumnoDto();
        responseDto.setId(alumnoGuardado.getId());
        responseDto.setNombre(alumnoGuardado.getNombre());
        responseDto.setApellido(alumnoGuardado.getApellido());
        responseDto.setDni(alumnoGuardado.getDni());
        responseDto.setMatricula(alumnoGuardado.getMatricula());
        responseDto.setDireccion(alumnoGuardado.getDireccion());
        responseDto.setEdad(alumnoGuardado.getEdad());

        return responseDto;
    }
    
    private AlumnoDto convertToDto(Alumno alumno) {
        if (alumno == null) {
            return null;
        }
        return new AlumnoDto(
            alumno.getId(),
            alumno.getNombre(),
            alumno.getApellido(),
            alumno.getDni(),
            alumno.getMatricula(),
            alumno.getDireccion(),
            alumno.getEdad()
        );
    }

    /**
     * Obtiene una lista de todos los alumnos disponibles.
     *
     * @return Una lista de AlumnoDTO.
     */
    public List<AlumnoDto> findAllAlumnos() {
        return alumnoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
