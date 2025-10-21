package com.backend.Backend.mappers;

import com.backend.Backend.dtos.ProductoDTO;
import com.backend.Backend.dtos.ProductoDonadoDTO;
import com.backend.Backend.models.Producto;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoMapper {
    private final CategoriaMapper categoriaMapper;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioService usuarioService;

    public Producto convertToEntity(ProductoDonadoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .categoria(categoriaMapper.convertToEntity(dto.getCategoria()))
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .usuario(usuarioService.getUsuarioById(dto.getIdUsuario()))
                .urlImagen(dto.getUrlImagen())
                .build();
    }

    public ProductoDonadoDTO convertToDonadorDTO(Producto producto) {
        return ProductoDonadoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(categoriaMapper.convertToDTO(producto.getCategoria()))
                .descripcion(producto.getDescripcion())
                .estado(producto.getEstado())
                .idUsuario(producto.getUsuario().getId())
                .urlImagen(producto.getUrlImagen())
                .build();
    }

    public ProductoDTO convertToDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(categoriaMapper.convertToDTO(producto.getCategoria()))
                .descripcion(producto.getDescripcion())
                .estado(producto.getEstado())
                .idUsuario(producto.getUsuario().getId())
                .urlImagen(producto.getUrlImagen())
                .solicitantes(producto.getSolicitantes() == null ? null :
                        producto.getSolicitantes().stream()
                                .map(usuarioMapper::convertUsuarioToDTO).collect(java.util.stream.Collectors.toList()))
                .build();
    }

    public Producto convertToEntity(ProductoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .categoria(categoriaMapper.convertToEntity(dto.getCategoria()))
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .usuario(usuarioService.getUsuarioById(dto.getIdUsuario()))
                .urlImagen(dto.getUrlImagen())
                .build();
    }
}
