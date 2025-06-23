package com.challenge.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private CursoMapper cursoMapper;

    //@Autowired
    //private RabbitMQSender rabbitMQSender;

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
     * Obtiene un curso segun su ID
     *
     * @param id id del curso que recibe
     * @return retorna un curso en caso de existir
     */
    public Optional<CursoDto> findCursoById(Long id) {
        Optional<Curso> cursoEntity = cursoRepository.findById(id);
        return cursoEntity.map(cursoMapper::toDto); 
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
        System.out.println("Iniciando procesamiento de inscripción asincrónica para Alumno ID: " + alumnoId + " al Curso ID: " + cursoId + " (Hilo: " + Thread.currentThread().getName() + ")");

        try {
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
            Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);

            if (alumnoOpt.isEmpty()) {
                System.err.println("Error en inscripción asincrónica: Alumno con ID " + alumnoId + " no encontrado.");
                return;
            }
            if (cursoOpt.isEmpty()) {
                System.err.println("Error en inscripción asincrónica: Curso con ID " + cursoId + " no encontrado.");
                return;
            }

            Alumno alumno = alumnoOpt.get();
            Curso curso = cursoOpt.get();

            if (curso.getAlumnosInscritos().contains(alumno)) {
                System.out.println("Alumno " + alumno.getNombre() + " ya está inscrito en el curso " + curso.getNombre() + ".");
                return;
            }

            System.out.println("Inscribiendo a " + alumno.getNombre() + " en " + curso.getNombre() + " en la base de datos...");
            Thread.sleep(2500);

            alumno.getCursos().add(curso);
            curso.getAlumnosInscritos().add(alumno);

            alumnoRepository.save(alumno);
            cursoRepository.save(curso);

            System.out.println("Inscripción de Alumno " + alumno.getNombre() + " al Curso " + curso.getNombre() + " completada en DB.");

            notificacionService.enviarCorreoConfirmacionMatricula(alumno.getEmail(), curso.getNombre());

            System.out.println("Procesamiento asíncrono de inscripción finalizado para Alumno " + alumno.getNombre() + " y Curso " + curso.getNombre() + ".");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Procesamiento de inscripción asincrónico interrumpido para Alumno ID: " + alumnoId + " y Curso ID: " + cursoId + ".");
        } catch (Exception e) {
            System.err.println("Error inesperado durante la inscripción asincrónica para Alumno ID: " + alumnoId + " y Curso ID: " + cursoId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}