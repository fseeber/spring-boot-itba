package com.challenge.controllers;

import com.challenge.dtos.MateriaDto;
import com.challenge.services.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    /**
     * Endpoint PUT para actualizar una materia existente.
     * @param id El ID de la materia a actualizar, tomado de la URL.
     * @param materiaDto El DTO con los datos actualizados de la materia, tomado del cuerpo de la solicitud (JSON).
     * @return Un ResponseEntity con el DTO de la materia actualizada y el estado HTTP 200 OK.
     * Retornará 404 Not Found si la materia no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MateriaDto> actualizarMateria(@PathVariable Long id, @RequestBody MateriaDto materiaDto) {
        MateriaDto materiaActualizada = materiaService.actualizarMateria(id, materiaDto);
        return new ResponseEntity<>(materiaActualizada, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MateriaDto> crearMateria(@RequestBody MateriaDto materiaDto) {
        MateriaDto nuevaMateria = materiaService.crearMateria(materiaDto);
        return new ResponseEntity<>(nuevaMateria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MateriaDto>> getAllMaterias() {
        List<MateriaDto> materias = materiaService.findAllMaterias();
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }
}