package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioProductosEntregadosResumen {
    private Long usuarioId;
    private String nombreUsuario;
    private long cantidadProductosEntregados;
}

