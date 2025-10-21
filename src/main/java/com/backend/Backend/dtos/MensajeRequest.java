package com.backend.Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MensajeRequest {
    private String mensaje;
    private Long idUsuario;
    private String sala;
}
