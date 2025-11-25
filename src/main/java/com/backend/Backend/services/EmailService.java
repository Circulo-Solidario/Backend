package com.backend.Backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Código de Recuperación de Cuenta - Círculo Solidario");
            message.setText("Tu código de recuperación es: " + codigo + "\n\nEste código expirará en 24 horas.");
            
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email de recuperación: " + e.getMessage());
        }
    }
}
