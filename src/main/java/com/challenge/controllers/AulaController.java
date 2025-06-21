package com.challenge.controllers;

import com.challenge.dtos.AulaDto;
import com.challenge.dtos.CursoDto;
import com.challenge.services.AulaService;

import java.util.List;

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