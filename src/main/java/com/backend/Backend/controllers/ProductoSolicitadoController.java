package com.backend.Backend.controllers;

import com.backend.Backend.dtos.CategoriaDTO;
import com.backend.Backend.dtos.ProductoSolicitadoDTO;
import com.backend.Backend.models.*;
import com.backend.Backend.repositories.CategoriaRepository;
import com.backend.Backend.repositories.ProductoRepository;
import com.backend.Backend.repositories.ProductoSolicitadoRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import com.backend.Backend.services.ProductoSolicitadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productosSolicitados")
public class ProductoSolicitadoController {
    @Autowired
    private ProductoSolicitadoService productoSolicitadoService;
    @Autowired
    private ProductoSolicitadoRepository productoSolicitadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @CrossOrigin
    @GetMapping
    public List<ProductoSolicitadoDTO> getAllProductosSolicitados() {
        return productoSolicitadoService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ProductoSolicitadoDTO getProductoSolicitadoById(@PathVariable Long id) {
        Optional<ProductoSolicitado> productoSolicitado = productoSolicitadoService.findById(id);
        return productoSolicitado.map(this::convertToDTO).orElse(null);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<ProductoSolicitado> crear(@RequestBody ProductoSolicitadoDTO dto) {
        ProductoSolicitado nuevo = productoSolicitadoService.save(dto);
        return ResponseEntity.ok(nuevo);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ProductoSolicitadoDTO updateProducto(@PathVariable Long id, @RequestBody ProductoSolicitadoDTO productoSolicitadoDTO) {
        productoSolicitadoDTO.setId(id);
        ProductoSolicitado updatedProducto = productoSolicitadoService.save(productoSolicitadoDTO);
        return convertToDTO(updatedProducto);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public void deleteProductoSolicitado(@PathVariable Long id) {
        productoSolicitadoService.deleteById(id);
    }

    private ProductoSolicitadoDTO convertToDTO(ProductoSolicitado productoSolicitado) {
        return ProductoSolicitadoDTO.builder()
                .id(productoSolicitado.getId())
                .idUsuario(productoSolicitado.getUsuario().getId())
                .idProducto(productoSolicitado.getProducto().getId())
                /*.nombre(productoSolicitado.getNombre())
                .categoria(convertCategoriaToDTO(productoSolicitado.getCategoria()))
                .descripcion(productoSolicitado.getDescripcion())
                .estado(productoSolicitado.getEstado())*/
                .build();
    }

    private ProductoSolicitado convertToEntity(ProductoSolicitadoDTO productoSolicitadoDTO) {
        Usuario usuario = usuarioRepository.findById(productoSolicitadoDTO.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + productoSolicitadoDTO.getIdUsuario()));
        Producto producto = productoRepository.findById(productoSolicitadoDTO.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoSolicitadoDTO.getIdProducto()));

        return ProductoSolicitado.builder()
                .usuario(usuario)
                .producto(producto)
                /*.nombre(productoSolicitadoDTO.getNombre())
                .categoria(convertCategoriaFromDTO(productoSolicitadoDTO.getCategoria()))
                .descripcion(productoSolicitadoDTO.getDescripcion())
                .estado(productoSolicitadoDTO.getEstado())*/
                .build();
    }

    private Categoria convertCategoriaFromDTO(CategoriaDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new IllegalArgumentException("La categoría es requerida");
        }

        return categoriaRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría con ID " + dto.getId() + " no existe"));
    }

    private CategoriaDTO convertCategoriaToDTO(Categoria categoria) {
        if (categoria == null) {
            return CategoriaDTO.builder()
                    .id(10L)
                    .nombre("Otros")
                    .descripcion("Otros artículos no categorizados")
                    .build();
        }
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}
