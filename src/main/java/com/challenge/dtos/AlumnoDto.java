package com.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoDto {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private String email;
    private Integer matricula;
    private String direccion;
    private Integer edad;
}
