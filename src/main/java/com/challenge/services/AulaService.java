package com.challenge.services;

import com.challenge.dtos.AulaDto;
import com.challenge.entities.Aula;
import com.challenge.mappers.AulaMapper;
import com.challenge.repositories.AulaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AulaService {

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private AulaMapper aulaMapper;

    /**
     * Elimina un aula por su ID y dispara un proceso asíncrono.
     * @param id El ID del aula a eliminar.
     * @throws ResponseStatusException Si el aula no se encuentra.
     */
    @Transactional
    public void eliminarAula(Long id) {
        if (!aulaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aula con ID " + id + " no encontrada para eliminar.");
        }
        aulaRepository.deleteById(id);
    }
    
    /**
     * Obtiene una lista de todos los aulas disponibles.
     *
     * @return Una lista de AulaDto.
     */
    public List<AulaDto> findAllAulas() {
        List<Aula> aulas = aulaRepository.findAll();
        return aulaMapper.toDtoList(aulas);
    }
}