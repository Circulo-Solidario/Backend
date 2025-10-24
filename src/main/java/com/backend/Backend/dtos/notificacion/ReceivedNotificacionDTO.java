package com.backend.Backend.dtos.notificacion;

import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.enums.TipoNotificaciones;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceivedNotificacionDTO {
    private Long id;
    private String mensaje;
    private UsuarioSimpleDTO deUsuario;
    private Date fechaNotificacion;
    private Date fechaVistaNotificacion;
    private TipoNotificaciones tipoNotificacion;
}
