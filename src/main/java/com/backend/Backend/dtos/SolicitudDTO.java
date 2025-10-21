package com.backend.Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    private Long id;
    private Long deUsuario;
    private Long aUsuario;
    private Long idProducto;
    /*private String nombre;
    private CategoriaDTO categoria;
    private String descripcion;
    private EstadoProducto estado;*/
}
