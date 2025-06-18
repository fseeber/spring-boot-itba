package com.challenge.controllers;

import com.challenge.dtos.AlumnoDto;
import com.challenge.services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService; // Inyecta el servicio de alumnos

    /**
     * Endpoint POST para guardar un nuevo alumno.
     * Recibe un AlumnoDto en el cuerpo de la solicitud (JSON).
     * @param alumnoDto El DTO del alumno a guardar.
     * @return Un ResponseEntity con el AlumnoDto guardado y el estado HTTP 201 Created.
     * Retornará un estado 409 Conflict si el DNI ya existe.
     */
    @PostMapping // Mapea las solicitudes POST a /api/alumnos a este método
    public ResponseEntity<AlumnoDto> guardarAlumno(@RequestBody AlumnoDto alumnoDto) {
        AlumnoDto alumnoGuardado = alumnoService.guardarAlumno(alumnoDto);
        return new ResponseEntity<>(alumnoGuardado, HttpStatus.CREATED);
    }
}