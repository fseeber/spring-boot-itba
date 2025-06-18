package com.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaDto {
    private Long id;
    private String numero;
    private Integer capacidad;
    private String ubicacion;
    private String observaciones;
}
