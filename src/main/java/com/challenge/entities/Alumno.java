package com.challenge.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alumnos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, nullable = false)
    private Integer dni;

    @Column(unique = true, nullable = false)
    private Integer matricula;
    
    private String direccion;
    private String email;
    private Integer edad;

    @ManyToMany(mappedBy = "alumnosInscriptos")
    private List<Curso> cursos = new ArrayList<>();

    public Alumno(String nombre, String apellido, String direccion, Integer matricula, Integer dni, String email, Integer edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.matricula = matricula;
        this.direccion = direccion;
        this.email = email;
        this.edad = edad;
    }
}
