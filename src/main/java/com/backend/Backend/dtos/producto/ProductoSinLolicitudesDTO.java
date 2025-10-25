package com.backend.Backend.dtos.producto;

import com.backend.Backend.dtos.categoria.CategoriaDTO;
import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoSinLolicitudesDTO {
    private Long id;
    private String nombre;
    private CategoriaDTO categoria;
    private String descripcion;
    private EstadoProducto estado;
    private UsuarioSimpleDTO usuario;
    private String urlImagen;
}
