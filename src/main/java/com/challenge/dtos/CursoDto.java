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
    private String carrera;
    private String detalle ;
    private String programa;    
}
