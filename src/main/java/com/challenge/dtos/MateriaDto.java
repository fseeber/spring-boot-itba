package com.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MateriaDto {
    private Long id;
    private String nombre;
    private String carrera;
    private String detalle ;
    private String programa;    
}
