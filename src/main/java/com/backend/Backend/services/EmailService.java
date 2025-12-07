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

    @Value("${brevo.api.key:}")
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
            log.info("Iniciando envío de código de recuperación a: {}", destinatario);

            // Obtener la API key de varias fuentes posibles
            String apiKey = resolveBrevoApiKey();

            if (apiKey == null || apiKey.isEmpty()) {
                log.error("ERROR: brevoApiKey es null o está vacío! Intenta con variable de entorno BREVO_API_KEY");
                throw new RuntimeException("API key de Brevo no configurada correctamente");
            }

            log.debug("API URL: {}", brevoApiUrl);
            log.debug("API Key length: {}", apiKey.length());
            log.debug("API Key first 10 chars: {}", apiKey.length() > 10 ? apiKey.substring(0, 10) : apiKey);
            log.debug("From Email: {}", fromEmail);

            // Crear los headers con la API key de Brevo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            log.debug("Headers configurados: Content-Type={}, api-key header presente: {}",
                headers.getContentType(), headers.containsKey("api-key"));

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
            log.debug("Request body: {}", jsonBody);

            // Crear la solicitud HTTP
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            // Realizar la solicitud POST a Brevo
            log.info("Enviando solicitud POST a Brevo...");
            ResponseEntity<String> response = restTemplate.postForEntity(brevoApiUrl, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error al enviar email. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Error al enviar el email. Status: " + response.getStatusCode());
            }

            log.info("Email de recuperación enviado exitosamente a: {}", destinatario);

        } catch (Exception e) {
            log.error("Error al enviar el email de recuperación a través de Brevo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar el email de recuperación a través de Brevo: " + e.getMessage());
        }
    }

    /**
     * Resuelve la API key de Brevo desde múltiples fuentes:
     * 1. Variable de entorno BREVO_API_KEY
     * 2. Property brevo.api.key del application.properties
     */
    private String resolveBrevoApiKey() {
        // Intentar obtener de variable de entorno primero
        String envApiKey = System.getenv("BREVO_API_KEY");
        if (envApiKey != null && !envApiKey.isEmpty()) {
            log.debug("API key cargada desde variable de entorno BREVO_API_KEY");
            return envApiKey;
        }

        // Luego intentar desde la propiedad
        if (brevoApiKey != null && !brevoApiKey.isEmpty()) {
            log.debug("API key cargada desde application.properties");
            return brevoApiKey;
        }

        log.error("No se encontró API key de Brevo en ninguna fuente");
        return null;
    }
}
