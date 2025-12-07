package com.backend.Backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.api.url}")
    private String brevoApiUrl;

    /*
    // Función anterior que usaba JavaMailSender
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
    */

    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        try {
            // Crear los headers con la API key de Brevo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            // Crear el body de la solicitud según la documentación de Brevo
            Map<String, Object> emailBody = new HashMap<>();

            // Crear el array "to" con el destinatario
            List<Map<String, String>> toList = new ArrayList<>();
            Map<String, String> toEmail = new HashMap<>();
            toEmail.put("email", destinatario);
            toList.add(toEmail);
            emailBody.put("to", toList);

            // Configurar el "sender" (Brevo requiere "sender" no "from")
            Map<String, String> senderMap = new HashMap<>();
            senderMap.put("email", fromEmail);
            senderMap.put("name", "Círculo Solidario");
            emailBody.put("sender", senderMap);

            // Asunto y contenido
            emailBody.put("subject", "Código de Recuperación de Cuenta - Círculo Solidario");
            emailBody.put("htmlContent", String.format(
                "<html><body>" +
                "<p>Tu código de recuperación es: <strong>%s</strong></p>" +
                "<p>Este código expirará en 24 horas.</p>" +
                "</body></html>", codigo
            ));

            String jsonBody = objectMapper.writeValueAsString(emailBody);

            // Crear la solicitud HTTP
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            // Realizar la solicitud POST a Brevo
            ResponseEntity<String> response = restTemplate.postForEntity(brevoApiUrl, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Error al enviar el email. Status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email de recuperación a través de Brevo: " + e.getMessage());
        }
    }
}
