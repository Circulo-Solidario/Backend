package com.backend.Backend.controllers;

import com.backend.Backend.dtos.MensajeRequest;
import com.backend.Backend.models.Mensaje;
import com.backend.Backend.services.MensajesService;
import com.pusher.rest.Pusher;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {
    private final Pusher pusher;
    private final MensajesService mensajesService;

    public ChatController(Pusher pusher, MensajesService messageService) {
        this.pusher = pusher;
        this.mensajesService = messageService;
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody MensajeRequest mensajeRequest) {
        try {

            if (mensajeRequest.getMessage() == null || mensajeRequest.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Mensaje vacío"));
            }

            Mensaje mensajeEntity = new Mensaje();
            mensajeEntity.setContent(mensajeRequest.getMessage());
            mensajeEntity.setRoom(mensajeRequest.getRoom());
            mensajeEntity.setUsername(mensajeRequest.getUsername());
            mensajeEntity.setTimestamp(Instant.now());

            Mensaje savedMessage = mensajesService.saveMessage(mensajeEntity);

            Map<String, Object> messageData = new HashMap<>();
            messageData.put("id", savedMessage.getId());
            messageData.put("message", savedMessage.getContent());
            messageData.put("username", savedMessage.getUsername());
            messageData.put("room", savedMessage.getRoom());
            messageData.put("timestamp", savedMessage.getTimestamp().toString());

            pusher.trigger(
                    mensajeRequest.getRoom(),
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
    public ResponseEntity<List<Mensaje>> getRoomMessages(@PathVariable String room) {
        try {
            List<Mensaje> messages = mensajesService.getRoomMessages(room);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/messages/{room}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String room) {
        mensajesService.deleteAllMessagesForRoom(room);
        return ResponseEntity.noContent().build();
    }
}

