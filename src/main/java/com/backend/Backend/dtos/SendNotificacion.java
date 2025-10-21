package com.backend.Backend.dtos;

import com.backend.Backend.models.TipoNotificaciones;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendNotificacion {
    private Long fromUser;
    private Long toUser;
    private String message;
    private TipoNotificaciones tipo;
}
