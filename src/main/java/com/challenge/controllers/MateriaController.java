package com.challenge.controllers;

import com.challenge.dtos.MateriaDto;
import com.challenge.services.MateriaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private static final Logger logger = LoggerFactory.getLogger(MateriaController.class);

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
        logger.info("MateriaController inicializado.");
    }

    /**
     * Endpoint PUT para actualizar una materia existente.
     * @param id El ID de la materia a actualizar, tomado de la URL.
     * @param materiaDto El DTO con los datos actualizados de la materia, tomado del cuerpo de la solicitud (JSON).
     * @return Un ResponseEntity con el DTO de la materia actualizada y el estado HTTP 200 OK.
     * Retornará 404 Not Found si la materia no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MateriaDto> actualizarMateria(@PathVariable Long id, @RequestBody MateriaDto materiaDto) {
        logger.info("Recibida solicitud para actualizar materia con ID: {}", id);
        try {
            MateriaDto materiaActualizada = materiaService.actualizarMateria(id, materiaDto);
            logger.info("Materia con ID {} actualizada exitosamente.", id);
            return new ResponseEntity<>(materiaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al actualizar materia con ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<MateriaDto> crearMateria(@RequestBody MateriaDto materiaDto) {
        logger.info("Recibida solicitud para crear materia: {}", materiaDto.getNombre());
        try {
            MateriaDto nuevaMateria = materiaService.crearMateria(materiaDto);
            logger.info("Materia creada exitosamente con ID: {}", nuevaMateria.getId());
            return new ResponseEntity<>(nuevaMateria, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear materia: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<MateriaDto>> getAllMaterias() {
        logger.info("Recibida solicitud para obtener todas las materias.");
        List<MateriaDto> materias = materiaService.findAllMaterias();
        logger.info("Se encontraron {} materias.", materias.size());
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    /**
     * Endpoint GET para obtener una materia por su ID.
     *
     * @param id El ID de la materia a buscar.
     * @return Un ResponseEntity con un MateriaDto si se encuentra, o un estado HTTP 404 NOT FOUND si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MateriaDto> getMateriaById(@PathVariable Long id) {
        logger.info("Recibida solicitud para obtener materia con ID: {}", id);
        Optional<MateriaDto> materia = materiaService.findMateriaById(id);

        return materia.map(value -> {
            logger.info("Materia con ID {} encontrada.", id);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            logger.warn("Materia con ID {} no encontrada.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     * Endpoint DELETE para eliminar una materia.
     * @param id El ID de la materia a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar materia con ID: {}", id);
        try {
            materiaService.eliminarMateria(id);
            logger.info("Materia con ID {} eliminada exitosamente.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error al eliminar materia con ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}