package com.challenge.services;

import com.challenge.dtos.AulaDto;
import com.challenge.entities.Aula;
import com.challenge.mappers.AulaMapper;
import com.challenge.repositories.AulaRepository;

import java.util.List;
import java.util.Optional;

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
    
    /**
     * Obtiene un aula segun su ID
     *
     * @param id id del aula que recibe
     * @return retorna un aula en caso de existir
     */
    public Optional<AulaDto> findAulaById(Long id) {
        Optional<Aula> aulaEntity = aulaRepository.findById(id);
        return aulaEntity.map(aulaMapper::toDto); 
    }

    /**
     * Guarda una nueva aula en la base de datos.
     * Recibe un AulaDto, lo convierte a entidad, lo guarda y devuelve el AulaDto resultante.
     * @param aulaDto El DTO del aula a guardar.
     * @return El DTO del aula guardado (con el ID generado).
     */
    @Transactional
    public AulaDto guardarAula(AulaDto aulaDto) {
        if (aulaRepository.findByNumero(aulaDto.getNumero()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El numero " + aulaDto.getNumero() + " ya está registrado para otra aula.");
        }
        
        Aula aula = aulaMapper.toEntity(aulaDto);

        Aula aulaGuardado = aulaRepository.save(aula);

        AulaDto responseDto = aulaMapper.toDto(aulaGuardado);

        return responseDto;
    }
}