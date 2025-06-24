package com.challenge.services;

import com.challenge.dtos.CursoConAlumnosDto;
import com.challenge.dtos.CursoDto;
import com.challenge.entities.Curso;
import com.challenge.entities.Materia;
import com.challenge.mappers.CursoMapper;
import com.challenge.entities.Alumno;
import com.challenge.entities.Aula;
import com.challenge.repositories.AlumnoRepository;
import com.challenge.repositories.CursoRepository;
import com.challenge.repositories.MateriaRepository;
import com.challenge.repositories.AulaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final MateriaRepository materiaRepository;
    private final AulaRepository aulaRepository;
    private final NotificacionService notificacionService;
    private final CursoMapper cursoMapper;

    public CursoService(
        AlumnoRepository alumnoRepository,
        CursoRepository cursoRepository,
        MateriaRepository materiaRepository,
        AulaRepository aulaRepository,
        NotificacionService notificacionService,
        CursoMapper cursoMapper) {
        
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
        this.materiaRepository = materiaRepository;
        this.aulaRepository = aulaRepository;
        this.notificacionService = notificacionService;
        this.cursoMapper = cursoMapper;
        logger.info("CursoService inicializado.");
    }

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
        logger.info("Intentando crear curso para materia ID: {} y aula ID: {}", 
                    cursoDto.getMateria().getId(), 
                    (cursoDto.getAula() != null ? cursoDto.getAula().getId() : "N/A"));

        Materia materia = materiaRepository.findById(cursoDto.getMateria().getId())
                .orElseThrow(() -> {
                    logger.warn("Materia con ID {} no encontrada al crear curso.", cursoDto.getMateria().getId());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Materia con ID " + cursoDto.getMateria().getId() + " no encontrada.");
                });

        Aula aula = null;
        if (cursoDto.getAula() != null && cursoDto.getAula().getId() != null) {
            aula = aulaRepository.findById(cursoDto.getAula().getId())
                    .orElseThrow(() -> {
                        logger.warn("Aula con ID {} no encontrada al crear curso.", cursoDto.getAula().getId());
                        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aula con ID " + cursoDto.getAula().getId() + " no encontrada.");
                    });
        }

        Curso curso = cursoMapper.toEntity(cursoDto);
        curso.setMateria(materia);
        curso.setAula(aula);

        Curso cursoGuardado = cursoRepository.save(curso);
        logger.info("Curso '{}' (ID: {}) creado exitosamente.", cursoGuardado.getNombre(), cursoGuardado.getId());

        return cursoMapper.toDto(cursoGuardado);
    }

    /**
     * Obtiene una lista de todos los cursos disponibles.
     *
     * @return Una lista de CursoDto.
     */
    public List<CursoDto> findAllCursos() {
        logger.info("Buscando todos los cursos.");
        List<Curso> cursos = cursoRepository.findAll();
        logger.info("Se encontraron {} cursos.", cursos.size());
        return cursoMapper.toDtoList(cursos);
    }

    /**
     * Obtiene una lista de todos los cursos junto con los alumnos inscritos en cada uno.
     * Esta operación puede ser costosa si hay muchos cursos y alumnos,
     *
     * @return Una lista de CursoConAlumnosDto.
     */
    @Transactional(readOnly = true)
    public List<CursoConAlumnosDto> findAllCursosConAlumnosInscriptos() {
        logger.info("Buscando todos los cursos con alumnos inscritos.");
        List<Curso> cursos = cursoRepository.findAllWithAlumnosInscriptos(); 
        logger.info("Se encontraron {} cursos con alumnos inscritos.", cursos.size());

        return cursos.stream()
                     .map(cursoMapper::toCursoConAlumnosDto)
                     .collect(Collectors.toList());
    }

    /**
     * Obtiene un curso segun su ID
     *
     * @param id id del curso que recibe
     * @return retorna un curso en caso de existir
     */
    public Optional<CursoDto> findCursoById(Long id) {
        logger.info("Buscando curso con ID: {}", id);
        Optional<Curso> cursoEntity = cursoRepository.findById(id);
        if (cursoEntity.isEmpty()) {
            logger.warn("Curso con ID {} no encontrado.", id);
        }
        return cursoEntity.map(cursoMapper::toDto); 
    }

    /**
     * Elimina un curso por su ID.
     * @param id El ID del curso a eliminar.
     * @throws ResponseStatusException Si el curso no se encuentra.
     */
    @Transactional
    public void eliminarCurso(Long id) {
        logger.info("Intentando eliminar curso con ID: {}", id);
        if (!cursoRepository.existsById(id)) {
            logger.warn("Intento de eliminación fallido: Curso con ID {} no encontrado.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso con ID " + id + " no encontrado para eliminar.");
        }
        cursoRepository.deleteById(id);
        logger.info("Curso con ID {} eliminado exitosamente.", id);
    }

    /**
     * Procesa la inscripción de un alumno a un curso de forma asincrónica.
     * Este método se ejecuta en un hilo separado, liberando el hilo principal de la solicitud HTTP.
     * Incluye la lógica de negocio para la inscripción y el envío de notificaciones.
     *
     * @param alumnoId El ID del alumno a inscribir.
     * @param cursoId El ID del curso al que se inscribirá el alumno.
     */
    @Async("threadPoolTaskExecutor")
    @Transactional
    public void procesarInscripcionAsincronica(Long alumnoId, Long cursoId) {
        logger.info("Iniciando procesamiento de inscripción asincrónica para Alumno ID: {} al Curso ID: {} (Hilo: {})",
                    alumnoId, cursoId, Thread.currentThread().getName());

        try {
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
            Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);

            if (alumnoOpt.isEmpty()) {
                logger.error("Error en inscripción asincrónica: Alumno con ID {} no encontrado.", alumnoId);
                return;
            }
            if (cursoOpt.isEmpty()) {
                logger.error("Error en inscripción asincrónica: Curso con ID {} no encontrado.", cursoId);
                return;
            }

            Alumno alumno = alumnoOpt.get();
            Curso curso = cursoOpt.get();

            if (curso.getAlumnosInscriptos().contains(alumno)) {
                logger.info("Alumno '{}' (ID: {}) ya está inscrito en el curso '{}' (ID: {}).",
                            alumno.getNombre(), alumno.getId(), curso.getNombre(), curso.getId());
                return;
            }

            logger.info("Inscribiendo a '{}' (ID: {}) en '{}' (ID: {}) en la base de datos...",
                        alumno.getNombre(), alumno.getId(), curso.getNombre(), curso.getId());
            Thread.sleep(2500);

            alumno.getCursos().add(curso);
            curso.getAlumnosInscriptos().add(alumno);

            alumnoRepository.save(alumno);
            cursoRepository.save(curso);

            logger.info("Inscripción de Alumno '{}' (ID: {}) al Curso '{}' (ID: {}) completada en DB.",
                        alumno.getNombre(), alumno.getId(), curso.getNombre(), curso.getId());

            notificacionService.enviarCorreoConfirmacionMatricula(alumno.getEmail(), curso.getNombre());
            logger.info("Notificación de matrícula enviada para Alumno '{}' al Curso '{}'.",
                        alumno.getNombre(), curso.getNombre());

            logger.info("Procesamiento asíncrono de inscripción finalizado para Alumno '{}' y Curso '{}'.",
                        alumno.getNombre(), curso.getNombre());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Procesamiento de inscripción asincrónico interrumpido para Alumno ID: {} y Curso ID: {}.",
                        alumnoId, cursoId, e);
        } catch (Exception e) {
            logger.error("Error inesperado durante la inscripción asincrónica para Alumno ID: {} y Curso ID: {}: {}",
                         alumnoId, cursoId, e.getMessage(), e);
        }
    }
}