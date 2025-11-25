package com.backend.Backend.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoRecuperacionDTO {
    private Boolean valido;
    private String mensaje;
    private Long usuarioId;
}
