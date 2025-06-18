package com.challenge.services;

import com.challenge.dtos.AulaDto;
import com.challenge.entities.Aula;
import com.challenge.repositories.AulaRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    private InternalProcessService internalProcessService;

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
        System.out.println("Aula con ID " + id + " eliminada de la base de datos.");

        internalProcessService.procesarAsincronoDespuesDeBorrarAula(id);
    }
    
    /**
     * Obtiene una lista de todos los cursos disponibles.
     *
     * @return Una lista de CursoDto.
     */
    public List<AulaDto> findAllAulas() {
        return aulaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AulaDto convertToDto(Aula aula) {
        if (aula == null) return null;
        return new AulaDto(aula.getId(), aula.getNumero(), aula.getCapacidad(), aula.getUbicacion(), aula.getObservaciones());
    }

    public Aula convertToEntity(AulaDto aulaDto) {
        if (aulaDto == null) return null;
        Aula aula = new Aula();
        aula.setId(aulaDto.getId());
        aula.setNumero(aulaDto.getNumero());
        aula.setCapacidad(aulaDto.getCapacidad());
        aula.setUbicacion(aulaDto.getUbicacion());
        aula.setObservaciones(aulaDto.getObservaciones());
        return aula;
    }
}