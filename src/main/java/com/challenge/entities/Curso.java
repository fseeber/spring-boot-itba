package com.challenge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    private Integer anio;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false) 
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "aula_id")
    private Aula aula;
}