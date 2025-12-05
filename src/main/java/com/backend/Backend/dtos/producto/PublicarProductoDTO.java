package com.backend.Backend.dtos.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicarProductoDTO {
    private String nombre;
    private Long categoriaId;
    private String descripcion;
    private Long idUsuario;
    private String urlImagen;
    private Double latitud;
    private Double longitud;
}
