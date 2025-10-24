package com.backend.Backend.controllers;

import com.backend.Backend.dtos.mensaje.MensajeRequest;
import com.backend.Backend.mappers.MensajeMapper;
import com.backend.Backend.models.Mensaje;
import com.backend.Backend.services.MensajesService;
import com.pusher.rest.Pusher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ChatController {
    private final Pusher pusher;
    private final MensajesService mensajesService;
    private final MensajeMapper mensajeMapper;

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody MensajeRequest mensajeRequest) {
        try {

            if (mensajeRequest.getMensaje() == null || mensajeRequest.getMensaje().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Mensaje vacío"));
            }

            Mensaje mensajeEntity = mensajeMapper.mensajeRequestToEntity(mensajeRequest);


            Mensaje savedMessage = mensajesService.saveMessage(mensajeEntity);

            Map<String, Object> messageData = new HashMap<>();
            messageData.put("id", savedMessage.getId());
            messageData.put("message", savedMessage.getMensaje());
            messageData.put("username", savedMessage.getUsuario().getAlias());
            messageData.put("room", savedMessage.getSala().getNombreSala());
            messageData.put("timestamp", savedMessage.getFechaMensaje());

            pusher.trigger(
                    mensajeRequest.getSala(),
                    "new-message",
                    messageData
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Mensaje enviado y guardado");
            response.put("messageId", savedMessage.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/messages/{room}")
    public ResponseEntity<List<Mensaje>> getRoomMessages(@PathVariable String nombreSala) {
        try {
            List<Mensaje> messages = mensajesService.getRoomMessages(nombreSala);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

