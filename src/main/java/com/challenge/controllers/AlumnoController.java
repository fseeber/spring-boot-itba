package com.challenge.controllers;

import com.challenge.dtos.AlumnoDto;
import com.challenge.services.AlumnoService;
import com.challenge.services.CursoService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    private final AlumnoService alumnoService;
    private final CursoService cursoService;

    public AlumnoController(AlumnoService alumnoService, CursoService cursoService) {
        this.alumnoService = alumnoService;
        this.cursoService = cursoService;
        logger.info("AlumnoController inicializado.");
    }

    /**
     * Endpoint POST para guardar un nuevo alumno.
     * Recibe un AlumnoDto en el cuerpo de la solicitud (JSON).
     * @param alumnoDto El DTO del alumno a guardar.
     * @return Un ResponseEntity con el AlumnoDto guardado y el estado HTTP 201 Created.
     * Retornará un estado 409 Conflict si el DNI ya existe.
     */
    @PostMapping
    public ResponseEntity<AlumnoDto> guardarAlumno(@RequestBody AlumnoDto alumnoDto) {
        logger.info("Recibida solicitud para guardar alumno: {}", alumnoDto.getDni());
        try {
            AlumnoDto alumnoGuardado = alumnoService.guardarAlumno(alumnoDto);
            logger.info("Alumno guardado exitosamente con ID: {}", alumnoGuardado.getId());
            return new ResponseEntity<>(alumnoGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al guardar alumno con DNI {}: {}", alumnoDto.getDni(), e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint GET para obtener la lista de todos los alumnos.
     *
     * @return Un ResponseEntity con una lista de AlumnoDto y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<AlumnoDto>> getAllAlumnos() {
        logger.info("Recibida solicitud para obtener todos los alumnos.");
        List<AlumnoDto> alumnos = alumnoService.findAllAlumnos();
        logger.info("Se encontraron {} alumnos.", alumnos.size());
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
        logger.info("Recibida solicitud para obtener alumno con ID: {}", id);
        Optional<AlumnoDto> alumno = alumnoService.findAlumnoById(id);

        return alumno.map(value -> {
            logger.info("Alumno con ID {} encontrado.", id);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            logger.warn("Alumno con ID {} no encontrado.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     * Endpoint DELETE para eliminar un alumno.
     * @param id El ID del alumno a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar alumno con ID: {}", id);
        try {
            alumnoService.eliminarAlumno(id);
            logger.info("Alumno con ID {} eliminado exitosamente.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error al eliminar alumno con ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint POST para inscribir a un alumno en un curso.
     * Este método recibe la petición HTTP y devuelve una respuesta inmediata al cliente,
     * indicando que la solicitud ha sido aceptada. El procesamiento real de la inscripción
     * se delega a un método asincrónico en el CursoService.
     *
     * @param alumnoId El ID del alumno que desea inscribirse.
     * @param cursoId El ID del curso al que el alumno desea inscribirse.
     * @return Un ResponseEntity con un mensaje informativo y el estado HTTP 202 Accepted.
     */
    @PostMapping("/{alumnoId}/inscribirCurso/{cursoId}")
    public ResponseEntity<String> inscribirAlumnoAcurso(
            @PathVariable Long alumnoId,
            @PathVariable Long cursoId) {

        logger.info("Solicitud de inscripción recibida para Alumno ID: {} en Curso ID: {} (Hilo Principal: {})",
                    alumnoId, cursoId, Thread.currentThread().getName());

        cursoService.procesarInscripcionAsincronica(alumnoId, cursoId);

        logger.info("Inscripción del Alumno {} al Curso {} delegada para procesamiento asincrónico.", alumnoId, cursoId);
        return new ResponseEntity<>("Solicitud de inscripción para el Alumno " + alumnoId + " al Curso " + cursoId + " ha sido recibida. Procesando en segundo plano...", HttpStatus.ACCEPTED);
    }
}