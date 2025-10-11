package com.backend.Backend.dtos;

import com.backend.Backend.models.EstadoProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSolicitadoDTO {
    private Long id;
    private Long idUsuario;
    private Long idProducto;
    /*private String nombre;
    private CategoriaDTO categoria;
    private String descripcion;
    private EstadoProducto estado;*/
}
