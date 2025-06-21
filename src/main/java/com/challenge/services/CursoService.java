package com.challenge.services;

import com.challenge.dtos.CursoDto;
import com.challenge.entities.Curso;
import com.challenge.entities.Materia;
import com.challenge.mappers.CursoMapper;
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

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private CursoMapper cursoMapper;

    @Autowired
    private RabbitMQSender rabbitMQSender;

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

        Curso curso = cursoMapper.toEntity(cursoDto);

        curso.setMateria(materia);
        curso.setAula(aula);

        Curso cursoGuardado = cursoRepository.save(curso);

        //rabbitMQSender.send(cursoMapper.toDto(cursoGuardado));

        return cursoMapper.toDto(cursoGuardado);
    }

    /**
     * Obtiene una lista de todos los cursos disponibles.
     *
     * @return Una lista de CursoDto.
     */
    public List<CursoDto> findAllCursos() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursoMapper.toDtoList(cursos);
    }

    /**
     * Elimina un curso por su ID.
     * @param id El ID del curso a eliminar.
     * @throws ResponseStatusException Si el curso no se encuentra.
     */
    @Transactional
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso con ID " + id + " no encontrado para eliminar.");
        }
        cursoRepository.deleteById(id);
    }
}