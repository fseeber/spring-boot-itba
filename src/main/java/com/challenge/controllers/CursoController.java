package com.challenge.controllers;

import com.challenge.dtos.CursoDto;
import com.challenge.services.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    /**
     * Endpoint POST para crear un nuevo curso.
     * Recibe un CursoDto en el cuerpo de la solicitud (JSON).
     * El proceso de validación de Materia/Aula y el disparo asíncrono
     * se manejan en el CursoService.
     *
     * @param cursoDto El DTO del curso a crear, enviado en el cuerpo de la solicitud.
     * @return Un ResponseEntity con el DTO del curso creado y el estado HTTP 201 Created.
     * Puede retornar un 400 Bad Request si la materia/aula no existen.
     */
    @PostMapping
    public ResponseEntity<CursoDto> crearCurso(@RequestBody CursoDto cursoDto) {
        CursoDto nuevoCurso = cursoService.crearCurso(cursoDto);
        return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
    }

    /**
     * Endpoint GET para obtener la lista de todos los cursos.
     *
     * @return Un ResponseEntity con una lista de CursoDto y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<CursoDto>> getAllCursos() {
        List<CursoDto> cursos = cursoService.findAllCursos();
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    /**
     * Endpoint GET para obtener un curso por su ID.
     *
     * @param id El ID del curso a buscar.
     * @return Un ResponseEntity con un CursoDto si se encuentra, o un estado HTTP 404 NOT FOUND si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CursoDto> getCursoById(@PathVariable Long id) {
        Optional<CursoDto> curso = cursoService.findCursoById(id);

        return curso.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint DELETE para eliminar un curso.
     * @param id El ID del curso a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}