package com.backend.Backend.mappers;

import com.backend.Backend.dtos.producto.ProductoSinLolicitudesDTO;
import com.backend.Backend.models.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoMapper {
    private final CategoriaMapper categoriaMapper;
    private final UsuarioMapper usuarioMapper;

    public ProductoSinLolicitudesDTO entityToProductoSinSolicitudes(Producto producto){
        return ProductoSinLolicitudesDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(categoriaMapper.convertToDTO(producto.getCategoria()))
                .descripcion(producto.getDescripcion())
                .estado(producto.getEstado())
                .usuario(usuarioMapper.mapEntityToUsuarioSimple(producto.getUsuario()))
                .urlImagen(producto.getUrlImagen())
                .latitud(producto.getLatitud())
                .longitud(producto.getLongitud())
                .build();
    }
}
