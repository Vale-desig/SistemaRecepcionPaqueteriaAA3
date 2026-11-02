package com.sistemarecepcion.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service 
public class CorreoService {

    private final JavaMailSender mailSender;

    public CorreoService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCodigoRecuperacion(String correo, String codigo) {

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(correo);
        mensaje.setSubject("Recuperación de contraseña - Sistema de Recepción");
        mensaje.setText(
            "Hola,\n\n"
            + "Has solicitado recuperar tu contraseña.\n\n"
            + "Tu código de recuperación es: " + codigo + "\n\n"
            + "Si no solicitaste este cambio, puedes ignorar este mensaje."
        );

        mailSender.send(mensaje);
    }
    
}
