package com.challenge.controllers;

import com.challenge.dtos.AulaDto;
import com.challenge.services.AulaService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    @Autowired
    private AulaService aulaService;

    /**
     * Endpoint DELETE para eliminar un aula.
     * @param id El ID del aula a eliminar.
     * @return Un ResponseEntity con estado HTTP 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAula(@PathVariable Long id) {
        aulaService.eliminarAula(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint GET para obtener la lista de todos las aulas.
     *
     * @return Un ResponseEntity con una lista de AulaDto y el estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<AulaDto>> getAllAulas() {
        List<AulaDto> cursos = aulaService.findAllAulas();
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }
    
    /**
     * Endpoint GET para obtener un aula por su ID.
     *
     * @param id El ID del aula a buscar.
     * @return Un ResponseEntity con un AulaDto si se encuentra, o un estado HTTP 404 NOT FOUND si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AulaDto> getAulaById(@PathVariable Long id) {
        Optional<AulaDto> aula = aulaService.findAulaById(id);

        return aula.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        AulaDto nuevoAula = aulaService.guardarAula(aulaDto);
        return new ResponseEntity<>(nuevoAula, HttpStatus.CREATED);
    }
}