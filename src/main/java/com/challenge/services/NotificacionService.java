package com.challenge.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    @Async("threadPoolTaskExecutor")
    public void enviarCorreoConfirmacionMatricula(String emailAlumno, String nombreCurso) {
        logger.info("Enviando correo de confirmación de matrícula a: {} para el curso: {} (Hilo: {})",
                    emailAlumno, nombreCurso, Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("El envío de correo de confirmación a {} fue interrumpido.", emailAlumno, e);
        }
        logger.info("Correo de confirmación enviado a: {}", emailAlumno);
    }
}