package com.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class CursoConAlumnosDto {
    private Long id;
    private String nombre;
    private String codigo;
    private int capacidadMaxima;
    private List<AlumnosInscriptosDto> alumnosInscriptos;
}
