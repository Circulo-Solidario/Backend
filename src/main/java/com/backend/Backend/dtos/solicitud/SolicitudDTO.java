package com.backend.Backend.dtos.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    private Long deUsuario;
    private Long aUsuario;
    private Long idProducto;
    private String mensaje;
}
