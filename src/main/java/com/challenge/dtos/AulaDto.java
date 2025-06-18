package com.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaDto {
    private Long id;
    private Integer piso;
    private String aula;
    private String edificio;
    private Integer capacidad;
}
