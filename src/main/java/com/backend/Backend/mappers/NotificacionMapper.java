package com.backend.Backend.mappers;

import com.backend.Backend.dtos.notificacion.ReceivedNotificacionDTO;
import com.backend.Backend.models.Notificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacionMapper {
    private final UsuarioMapper usuarioMapper;

    public ReceivedNotificacionDTO mapEntityToReceiveNotificacion(Notificacion notificacion) {
        return ReceivedNotificacionDTO.builder()
                .id(notificacion.getId())
                .mensaje(notificacion.getMensaje())
                .deUsuario(usuarioMapper.mapEntityToUsuarioSimple(notificacion.getDeUsuario()))
                .fechaNotificacion(notificacion.getFechaNotificacion())
                .fechaVistaNotificacion(notificacion.getFechaVistaNotificacion())
                .tipoNotificacion(notificacion.getTipoNotificacion())
                .build();
    }
}
