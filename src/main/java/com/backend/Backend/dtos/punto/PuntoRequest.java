package com.backend.Backend.dtos.punto;

import lombok.Data;

@Data
public class PuntoRequest {
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private Long usuarioRegistroId;
}
