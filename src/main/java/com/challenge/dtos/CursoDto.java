package com.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer anio;
    private MateriaDto materia;
    private AulaDto aula;   
}
