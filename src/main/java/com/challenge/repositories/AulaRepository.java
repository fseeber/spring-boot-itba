package com.challenge.repositories;

import com.challenge.entities.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    Optional<Aula> findByNumero(String numero);
}
