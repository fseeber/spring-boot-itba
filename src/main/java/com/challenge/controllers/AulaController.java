package com.challenge.controllers;

import com.challenge.dtos.AulaDto;
import com.challenge.services.AulaService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    private static final Logger logger = LoggerFactory.getLogger(AulaController.class);

    private final AulaService aulaService;
    
    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
        logger.info("AulaController inicializado.");
    }

    /**
     * Endpoint DELETE para eliminar un aula.
     * @param id El ID del aula a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAula(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar aula con ID: {}", id);
        try {
            aulaService.eliminarAula(id);
            logger.info("Aula con ID {} eliminada exitosamente.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error al eliminar aula con ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint GET para obtener la lista de todos las aulas.
     *
     * @return Un ResponseEntity con una lista de AulaDto y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<AulaDto>> getAllAulas() {
        logger.info("Recibida solicitud para obtener todas las aulas.");
        List<AulaDto> aulas = aulaService.findAllAulas();
        logger.info("Se encontraron {} aulas.", aulas.size());
        return new ResponseEntity<>(aulas, HttpStatus.OK);
    }
    
    /**
     * Endpoint GET para obtener un aula por su ID.
     *
     * @param id El ID del aula a buscar.
     * @return Un ResponseEntity con un AulaDto si se encuentra, o un estado HTTP 404 NOT FOUND si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AulaDto> getAulaById(@PathVariable Long id) {
        logger.info("Recibida solicitud para obtener aula con ID: {}", id);
        Optional<AulaDto> aula = aulaService.findAulaById(id);

        return aula.map(value -> {
            logger.info("Aula con ID {} encontrada.", id);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            logger.warn("Aula con ID {} no encontrada.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     * Endpoint POST para crear una nueva aula.
     * Recibe un AulaDto en el cuerpo de la solicitud (JSON).
     *
     * @param aulaDto El DTO del aula a crear, enviado en el cuerpo de la solicitud.
     * @return Un ResponseEntity con el DTO del aula creado y el estado HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<AulaDto> crearAula(@RequestBody AulaDto aulaDto) {
        logger.info("Recibida solicitud para crear aula.");
        try {
            AulaDto nuevoAula = aulaService.guardarAula(aulaDto);
            logger.info("Aula creada exitosamente con ID: {}", nuevoAula.getId());
            return new ResponseEntity<>(nuevoAula, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear aula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}