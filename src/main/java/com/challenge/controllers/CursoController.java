package com.challenge.controllers;

import com.challenge.dtos.CursoConAlumnosDto;
import com.challenge.dtos.CursoDto;
import com.challenge.services.CursoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);

    private CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
        logger.info("CursoController inicializado.");
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
        logger.info("Recibida solicitud para crear curso: {}", cursoDto.getNombre());
        try {
            CursoDto nuevoCurso = cursoService.crearCurso(cursoDto);
            logger.info("Curso creado exitosamente con ID: {}", nuevoCurso.getId());
            return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear curso: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint GET para obtener la lista de todos los cursos.
     *
     * @return Un ResponseEntity con una lista de CursoDto y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<CursoDto>> getAllCursos() {
        logger.info("Recibida solicitud para obtener todos los cursos.");
        List<CursoDto> cursos = cursoService.findAllCursos();
        logger.info("Se encontraron {} cursos.", cursos.size());
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
        logger.info("Recibida solicitud para obtener curso con ID: {}", id);
        Optional<CursoDto> curso = cursoService.findCursoById(id);

        return curso.map(value -> {
            logger.info("Curso con ID {} encontrado.", id);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            logger.warn("Curso con ID {} no encontrado.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }
    
    /**
     * Endpoint GET para obtener la lista de todos los cursos con sus alumnos inscritos.
     *
     * @return Un ResponseEntity con una lista de CursoConAlumnosDto y el estado HTTP 200 OK.
     */
    @GetMapping("/con-alumnos")
    public ResponseEntity<List<CursoConAlumnosDto>> getAllCursosConAlumnos() {
        logger.info("Recibida solicitud para obtener todos los cursos con alumnos inscritos.");
        List<CursoConAlumnosDto> cursos = cursoService.findAllCursosConAlumnosInscriptos();
        logger.info("Se encontraron {} cursos con alumnos inscritos.", cursos.size());
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }
    
    /**
     * Endpoint DELETE para eliminar un curso.
     * @param id El ID del curso a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar curso con ID: {}", id);
        try {
            cursoService.eliminarCurso(id);
            logger.info("Curso con ID {} eliminado exitosamente.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error al eliminar curso con ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}