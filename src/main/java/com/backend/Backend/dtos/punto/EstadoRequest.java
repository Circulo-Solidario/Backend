package com.backend.Backend.dtos.punto;

import lombok.Data;

@Data
public class EstadoRequest {
    private String estado;
    private Long usuarioAyudoId;
}
