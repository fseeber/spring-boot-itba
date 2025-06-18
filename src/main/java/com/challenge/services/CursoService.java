package com.challenge.services;

import com.challenge.dtos.CursoDto;
import com.challenge.dtos.MateriaDto;
import com.challenge.dtos.AulaDto;
import com.challenge.entities.Curso;
import com.challenge.entities.Materia;
import com.challenge.entities.Aula;
import com.challenge.repositories.CursoRepository;
import com.challenge.repositories.MateriaRepository;
import com.challenge.repositories.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private AulaRepository aulaRepository;

    /**
     * Crea y guarda un nuevo curso en la base de datos.
     * Valida que la materia y el aula (si se proporciona) existan.
     * Después de guardar, dispara un proceso asíncrono.
     *
     * @param cursoDto El DTO del curso a crear.
     * @return El DTO del curso guardado, incluyendo su ID y las relaciones resueltas.
     * @throws ResponseStatusException Si la materia o el aula asociada no se encuentran.
     */
    @Transactional
    public CursoDto crearCurso(CursoDto cursoDto) {
        Materia materia = materiaRepository.findById(cursoDto.getMateria().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Materia con ID " + cursoDto.getMateria().getId() + " no encontrada."));

        Aula aula = null;
        if (cursoDto.getAula() != null && cursoDto.getAula().getId() != null) {
            aula = aulaRepository.findById(cursoDto.getAula().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aula con ID " + cursoDto.getAula().getId() + " no encontrada."));
        }

        Curso curso = new Curso();
        curso.setNombre(cursoDto.getNombre());
        curso.setDescripcion(cursoDto.getDescripcion());
        curso.setAnio(cursoDto.getAnio());
        curso.setMateria(materia);
        curso.setAula(aula);

        Curso cursoGuardado = cursoRepository.save(curso);

        return convertToDto(cursoGuardado);
    }

    /**
     * Obtiene una lista de todos los cursos disponibles.
     *
     * @return Una lista de CursoDto.
     */
    public List<CursoDto> findAllCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Curso a su correspondiente DTO.
     * Maneja las relaciones con Materia y Aula, mapeándolas a sus respectivos DTOs.
     *
     * @param curso La entidad Curso a convertir.
     * @return Un CursoDto.
     */
    private CursoDto convertToDto(Curso curso) {
        if (curso == null) {
            return null;
        }

        MateriaDto materiaDto = null;
        if (curso.getMateria() != null) {
            materiaDto = new MateriaDto(
                curso.getMateria().getId(),
                curso.getMateria().getNombre(),
                curso.getMateria().getCreditos(),
                curso.getMateria().getCarrera(),
                curso.getMateria().getDetalle(),
                curso.getMateria().getPrograma()
            );
        }

        AulaDto aulaDto = null;
        if (curso.getAula() != null) {
            aulaDto = new AulaDto(
                curso.getAula().getId(),
                curso.getAula().getNumero(),
                curso.getAula().getCapacidad(),
                curso.getAula().getUbicacion(),
                curso.getAula().getObservaciones()
            );
        }

        return new CursoDto(
            curso.getId(),
            curso.getNombre(),
            curso.getDescripcion(),
            curso.getAnio(),
            materiaDto,
            aulaDto
        );
    }
}