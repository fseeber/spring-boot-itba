package com.challenge.controllers;

import com.challenge.dtos.AlumnoDto;
import com.challenge.services.AlumnoService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    /**
     * Endpoint POST para guardar un nuevo alumno.
     * Recibe un AlumnoDto en el cuerpo de la solicitud (JSON).
     * @param alumnoDto El DTO del alumno a guardar.
     * @return Un ResponseEntity con el AlumnoDto guardado y el estado HTTP 201 Created.
     * Retornará un estado 409 Conflict si el DNI ya existe.
     */
    @PostMapping
    public ResponseEntity<AlumnoDto> guardarAlumno(@RequestBody AlumnoDto alumnoDto) {
        AlumnoDto alumnoGuardado = alumnoService.guardarAlumno(alumnoDto);
        return new ResponseEntity<>(alumnoGuardado, HttpStatus.CREATED);
    }

    /**
     * Endpoint GET para obtener la lista de todos los alumnos.
     *
     * @return Un ResponseEntity con una lista de AlumnoDto y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<AlumnoDto>> getAllAulas() {
        List<AlumnoDto> alumnos = alumnoService.findAllAlumnos();
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    /**
     * Endpoint GET para obtener un alumno por su ID.
     *
     * @param id El ID del alumno a buscar.
     * @return Un ResponseEntity con un AlumnoDto si se encuentra, o un estado HTTP 404 NOT FOUND si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDto> getAlumnoById(@PathVariable Long id) {
        Optional<AlumnoDto> alumno = alumnoService.findAlumnoById(id);

        return alumno.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint DELETE para eliminar un alumno.
     * @param id El ID del alumno a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id) {
        alumnoService.eliminarAlumno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}