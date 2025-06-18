package com.challenge.services;

import com.challenge.dtos.CursoDto;
import com.challenge.entities.Curso;
import com.challenge.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternalProcessService {

    private static final Logger logger = LoggerFactory.getLogger(InternalProcessService.class);
    private final RestTemplate restTemplate;

    @Autowired
    private CursoRepository cursoRepository;

    public InternalProcessService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Proceso asíncrono que se dispara al borrar un Aula.
     * Chequea los cursos asociados a esa aula y los desvincula.
     * @param aulaId El ID del aula que fue borrada.
     */
    @Async
    public void procesarAsincronoDespuesDeBorrarAula(Long aulaId) {
        logger.info("INICIO del proceso asíncrono post-borrado de Aula ID: {} - Hilo: {}", aulaId, Thread.currentThread().getName());

        try {
            Thread.sleep(2000); //2 segundos

            String cursosApiUrl = "http://localhost:8080/api/cursos";
            logger.info("Llamando a la API interna de Cursos para obtener todos: {}", cursosApiUrl);

            ResponseEntity<CursoDto[]> response = restTemplate.getForEntity(cursosApiUrl, CursoDto[].class);
            List<CursoDto> todosLosCursos = Arrays.asList(response.getBody());

            logger.info("Cursos obtenidos de la API interna: {} cursos.", todosLosCursos.size());

            List<Curso> cursosAActualizar = todosLosCursos.stream()
                .filter(cursoDto -> cursoDto.getAula() != null && cursoDto.getAula().getId().equals(aulaId))
                .map(cursoDto -> cursoRepository.findById(cursoDto.getId()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

            if (!cursosAActualizar.isEmpty()) {
                logger.info("Se encontraron {} cursos asociados a la Aula ID {}. Desvinculándolos...", cursosAActualizar.size(), aulaId);
                for (Curso curso : cursosAActualizar) {
                    curso.setAula(null);
                    cursoRepository.save(curso);
                    logger.info("Curso ID {} desvinculado de Aula ID {}.", curso.getId(), aulaId);
                }
            } else {
                logger.info("No se encontraron cursos asociados a la Aula ID {}.", aulaId);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Proceso asíncrono (borrado de Aula) interrumpido para Aula ID {}: {}", aulaId, e.getMessage());
        } catch (Exception e) {
            logger.error("Error en el proceso asíncrono (borrado de Aula) para Aula ID {}: {}", aulaId, e.getMessage());
        }
        logger.info("FIN del proceso asíncrono post-borrado de Aula ID: {} - Hilo: {}", aulaId, Thread.currentThread().getName());
    }
}