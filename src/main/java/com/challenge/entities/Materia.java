package com.challenge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "materias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
    private Integer creditos;
    private String carrera;
    private String detalle;
    private String programa;
    
    public Materia(String nombre, Integer creditos, String carrera, String detalle, String programa) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.carrera = carrera;
        this.detalle = detalle;
        this.programa = programa;
    }
    
}