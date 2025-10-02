package com.backend.Backend.dtos;

import com.backend.Backend.models.EstadoProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDonadoDTO {
    private Long id;
    private String nombre;
    private CategoriaDTO categoria;
    private String descripcion;
    private EstadoProducto estado;
    private Long idUsuario;
    private String urlImagen;
}
