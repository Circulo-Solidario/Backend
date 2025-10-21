package com.backend.Backend.mappers;

import com.backend.Backend.dtos.SendNotificacion;
import com.backend.Backend.models.Notificacion;
import com.backend.Backend.services.ProductoService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class NotificacionMapper {
    private final UsuarioService usuarioService;

    public Notificacion mapToEntity(SendNotificacion sendNotificacion) {
        return Notificacion.builder()
                .AUsuario(usuarioService.getUsuarioById(sendNotificacion.getToUser()))
                .deUsuario(usuarioService.getUsuarioById(sendNotificacion.getFromUser()))
                .mensaje(sendNotificacion.getMessage())
                .build();
    }
}
