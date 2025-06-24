package com.challenge.services;

import com.challenge.dtos.AulaDto;
import com.challenge.entities.Aula;
import com.challenge.mappers.AulaMapper;
import com.challenge.repositories.AulaRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AulaService {

    private static final Logger logger = LoggerFactory.getLogger(AulaService.class);

    private final AulaRepository aulaRepository;
    private final AulaMapper aulaMapper;

    public AulaService(AulaRepository aulaRepository, AulaMapper aulaMapper) {
        this.aulaRepository = aulaRepository;
        this.aulaMapper = aulaMapper;
        logger.info("AulaService inicializado.");
    }

    /**
     * Elimina un aula por su ID y dispara un proceso asíncrono.
     * @param id El ID del aula a eliminar.
     * @throws ResponseStatusException Si el aula no se encuentra.
     */
    @Transactional
    public void eliminarAula(Long id) {
        logger.info("Intentando eliminar aula con ID: {}", id);
        if (!aulaRepository.existsById(id)) {
            logger.warn("Intento de eliminación fallido: Aula con ID {} no encontrada.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aula con ID " + id + " no encontrada para eliminar.");
        }
        aulaRepository.deleteById(id);
        logger.info("Aula con ID {} eliminada exitosamente.", id);
    }
    
    /**
     * Obtiene una lista de todos los aulas disponibles.
     *
     * @return Una lista de AulaDto.
     */
    public List<AulaDto> findAllAulas() {
        logger.info("Buscando todas las aulas.");
        List<Aula> aulas = aulaRepository.findAll();
        logger.info("Se encontraron {} aulas.", aulas.size());
        return aulaMapper.toDtoList(aulas);
    }
    
    /**
     * Obtiene un aula segun su ID
     *
     * @param id id del aula que recibe
     * @return retorna un aula en caso de existir
     */
    public Optional<AulaDto> findAulaById(Long id) {
        logger.info("Buscando aula con ID: {}", id);
        Optional<Aula> aulaEntity = aulaRepository.findById(id);
        if (aulaEntity.isEmpty()) {
            logger.warn("Aula con ID {} no encontrada.", id);
        }
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
        logger.info("Intentando guardar nueva aula con número: {}", aulaDto.getNumero());
        if (aulaRepository.findByNumero(aulaDto.getNumero()).isPresent()) {
            logger.warn("Intento de guardar aula fallido: El número de aula {} ya está registrado.", aulaDto.getNumero());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El numero " + aulaDto.getNumero() + " ya está registrado para otra aula.");
        }
        
        Aula aula = aulaMapper.toEntity(aulaDto);
        Aula aulaGuardado = aulaRepository.save(aula);
        logger.info("Aula guardada exitosamente con ID: {}", aulaGuardado.getId());

        return aulaMapper.toDto(aulaGuardado);
    }
}