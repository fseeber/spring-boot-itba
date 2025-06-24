package com.challenge.services;

import com.challenge.dtos.MateriaDto;
import com.challenge.entities.Materia;
import com.challenge.mappers.MateriaMapper;
import com.challenge.repositories.MateriaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {

    private static final Logger logger = LoggerFactory.getLogger(MateriaService.class);

    private final MateriaRepository materiaRepository;
    private final MateriaMapper materiaMapper;
    
    public MateriaService(MateriaRepository materiaRepository, MateriaMapper materiaMapper) {
        this.materiaRepository = materiaRepository;
        this.materiaMapper = materiaMapper;
        logger.info("MateriaService inicializado.");
    }

    /**
     * Actualiza una materia existente en la base de datos.
     * @param id El ID de la materia a actualizar.
     * @param materiaDto El DTO con los datos actualizados de la materia.
     * @return El DTO de la materia actualizada.
     * @throws ResponseStatusException Si la materia con el ID dado no se encuentra.
     */
    @Transactional
    public MateriaDto actualizarMateria(Long id, MateriaDto materiaDto) {
        logger.info("Intentando actualizar materia con ID: {}", id);
        Optional<Materia> materiaOptional = materiaRepository.findById(id);

        if (materiaOptional.isEmpty()) {
            logger.warn("Intento de actualización fallido: Materia con ID {} no encontrada.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia con ID " + id + " no encontrada.");
        }

        Materia materiaExistente = materiaOptional.get();

        if (materiaDto.getNombre() != null) {
            materiaExistente.setNombre(materiaDto.getNombre());
        }
        if (materiaDto.getCreditos() != null) {
            materiaExistente.setCreditos(materiaDto.getCreditos());
        }
        if (materiaDto.getCarrera() != null) {
            materiaExistente.setCarrera(materiaDto.getCarrera());
        }
        if (materiaDto.getDetalle() != null) {
            materiaExistente.setDetalle(materiaDto.getDetalle());
        }
        if (materiaDto.getPrograma() != null) {
            materiaExistente.setPrograma(materiaDto.getPrograma());
        }

        Materia materiaActualizada = materiaRepository.save(materiaExistente);
        logger.info("Materia con ID {} actualizada exitosamente.", materiaActualizada.getId());

        return materiaMapper.toDto(materiaActualizada);
    }

    @Transactional
    public MateriaDto crearMateria(MateriaDto materiaDto) {
        logger.info("Intentando crear nueva materia con nombre: {}", materiaDto.getNombre());
        if (materiaRepository.findByNombre(materiaDto.getNombre()).isPresent()) {
            logger.warn("Intento de creación de materia fallido: El nombre '{}' ya existe.", materiaDto.getNombre());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de materia '" + materiaDto.getNombre() + "' ya existe.");
        }

        Materia materia = new Materia();
        materia.setNombre(materiaDto.getNombre());
        materia.setCreditos(materiaDto.getCreditos());
        materia.setCarrera(materiaDto.getCarrera());
        materia.setDetalle(materiaDto.getDetalle());
        materia.setPrograma(materiaDto.getPrograma());

        Materia nuevaMateria = materiaRepository.save(materia);
        logger.info("Materia '{}' creada exitosamente con ID: {}", nuevaMateria.getNombre(), nuevaMateria.getId());

        return new MateriaDto(
            nuevaMateria.getId(),
            nuevaMateria.getNombre(),
            nuevaMateria.getCreditos(),
            nuevaMateria.getCarrera(),
            nuevaMateria.getDetalle(),
            nuevaMateria.getPrograma()
        );
    }

    public List<MateriaDto> findAllMaterias() {
        logger.info("Buscando todas las materias.");
        List<Materia> materias = materiaRepository.findAll();
        logger.info("Se encontraron {} materias.", materias.size());
        return materiaMapper.toDtoList(materias);
    }

    /**
     * Obtiene una materia segun su ID
     *
     * @param id id de la materia que recibe
     * @return retorna una materia en caso de existir
     */
    public Optional<MateriaDto> findMateriaById(Long id) {
        logger.info("Buscando materia con ID: {}", id);
        Optional<Materia> materiaEntity = materiaRepository.findById(id);
        if (materiaEntity.isEmpty()) {
            logger.warn("Materia con ID {} no encontrada.", id);
        }
        return materiaEntity.map(materiaMapper::toDto); 
    }

    /**
     * Elimina una materia por su ID.
     * @param id El ID de la materia a eliminar.
     * @throws ResponseStatusException Si la materia no se encuentra.
     */
    @Transactional
    public void eliminarMateria(Long id) {
        logger.info("Intentando eliminar materia con ID: {}", id);
        if (!materiaRepository.existsById(id)) {
            logger.warn("Intento de eliminación fallido: Materia con ID {} no encontrada.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia con ID " + id + " no encontrado para eliminar.");
        }
        materiaRepository.deleteById(id);
        logger.info("Materia con ID {} eliminada exitosamente.", id);
    }
}