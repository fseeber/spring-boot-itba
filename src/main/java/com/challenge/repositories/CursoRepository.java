package com.challenge.repositories;

import com.challenge.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query("SELECT c FROM Curso c JOIN FETCH c.alumnosInscriptos")
    List<Curso> findAllWithAlumnosInscriptos();
}