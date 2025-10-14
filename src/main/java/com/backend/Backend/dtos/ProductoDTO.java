package com.backend.Backend.dtos;

import com.backend.Backend.models.EstadoProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.backend.Backend.dtos.UsuarioDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private CategoriaDTO categoria;
    private String descripcion;
    private EstadoProducto estado;
    private Long idUsuario;
    private String urlImagen;
    private List<UsuarioDTO> solicitantes;
}
