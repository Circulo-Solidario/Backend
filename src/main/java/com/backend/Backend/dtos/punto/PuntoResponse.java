package com.backend.Backend.dtos.punto;

import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntoResponse {
    private Long id;
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private String estado;
    private UsuarioSimpleDTO usuarioRegistro;
    private UsuarioSimpleDTO usuarioAyudo;
}
