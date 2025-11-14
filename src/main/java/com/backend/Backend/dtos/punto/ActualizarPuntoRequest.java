package com.backend.Backend.dtos.punto;

import lombok.Data;

@Data
public class ActualizarPuntoRequest {
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private String estado;
}
