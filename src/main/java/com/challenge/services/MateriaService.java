package com.challenge.services;

import com.challenge.dtos.MateriaDto;
import com.challenge.entities.Materia;
import com.challenge.mappers.MateriaMapper;
import com.challenge.repositories.MateriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;

    private final MateriaMapper materiaMapper;
    

    public MateriaService(MateriaRepository materiaRepository, MateriaMapper materiaMapper) {
        this.materiaRepository = materiaRepository;
        this.materiaMapper = materiaMapper;
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
        Optional<Materia> materiaOptional = materiaRepository.findById(id);

        if (materiaOptional.isEmpty()) {
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

        MateriaDto responseDto = materiaMapper.toDto(materiaActualizada);

        return responseDto;
    }

    @Transactional
    public MateriaDto crearMateria(MateriaDto materiaDto) {
        if (materiaRepository.findByNombre(materiaDto.getNombre()).isPresent()) {
             throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de materia '" + materiaDto.getNombre() + "' ya existe.");
        }

        Materia materia = new Materia();
        materia.setNombre(materiaDto.getNombre());
        materia.setCreditos(materiaDto.getCreditos());
        materia.setCarrera(materiaDto.getCarrera());
        materia.setDetalle(materiaDto.getDetalle());
        materia.setPrograma(materiaDto.getPrograma());

        Materia nuevaMateria = materiaRepository.save(materia);

        return new MateriaDto(
            nuevaMateria.getId(),
            nuevaMateria.getNombre(),
            nuevaMateria.getCreditos(),
            nuevaMateria.getCarrera(),
            nuevaMateria.getDetalle(),
            nuevaMateria.getPrograma()
        );
    }

    public java.util.List<MateriaDto> findAllMaterias() {
        List<Materia> materias = materiaRepository.findAll();
        return materiaMapper.toDtoList(materias);
    }

    /**
     * Obtiene una materia segun su ID
     *
     * @param id id de la materia que recibe
     * @return retorna una materia en caso de existir
     */
    public Optional<MateriaDto> findMateriaById(Long id) {
        Optional<Materia> materiaEntity = materiaRepository.findById(id);
        return materiaEntity.map(materiaMapper::toDto); 
    }

     /**
     * Elimina una materia por su ID.
     * @param id El ID de la materia a eliminar.
     * @throws ResponseStatusException Si la materia no se encuentra.
     */
    @Transactional
    public void eliminarMateria(Long id) {
        if (!materiaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia con ID " + id + " no encontrado para eliminar.");
        }
        materiaRepository.deleteById(id);
    }
}