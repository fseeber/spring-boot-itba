package com.challenge.entities;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "cursos_alumnos",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "alumno_id")
    )
    private List<Alumno> alumnosInscritos = new ArrayList<>();

    public Curso(String nombre, String descripcion, Integer anio, Materia materia, Aula aula) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.anio = anio;
        this.materia = materia;
        this.aula = aula;
    }
    
}