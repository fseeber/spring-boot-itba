package com.challenge.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    @Async("threadPoolTaskExecutor")
    public void enviarCorreoConfirmacionMatricula(String emailAlumno, String nombreCurso) {
        System.out.println("Enviando correo de confirmación de matrícula a: " + emailAlumno + " para el curso: " + nombreCurso + " (Hilo: " + Thread.currentThread().getName() + ")");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupción al enviar correo.");
        }
        System.out.println("Correo de confirmación enviado a: " + emailAlumno);
    }
}
