package com.backend.Backend.controllers;

import com.backend.Backend.dtos.ProductoDTO;
import com.backend.Backend.dtos.ProductoDonadoDTO;
import com.backend.Backend.models.Producto;
import com.backend.Backend.repositories.ProductoRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import com.backend.Backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @CrossOrigin
    @GetMapping
    public List<ProductoDTO> getAllProductos() {
        return productoService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping("/donador")
    public ResponseEntity<ProductoDonadoDTO> createProductoDonador(@RequestBody ProductoDonadoDTO dto) {
        Producto producto = convertToEntity(dto);
        Producto savedProducto = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDonadorDTO(savedProducto));
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        Optional<Producto> existingProducto = productoService.findById(id);
        if (existingProducto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = convertToEntity(dto);
        producto.setId(id);
        Producto updatedProducto = productoService.save(producto);
        return ResponseEntity.ok(convertToDTO(updatedProducto));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @GetMapping("/categoria/{categoria}")
    public List<ProductoDTO> getProductosByCategoria(@PathVariable String categoria) {
        return productoService.findAllByCategoria(categoria).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Producto convertToEntity(ProductoDonadoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .categoria(dto.getCategoria())
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .idUsuario(dto.getIdUsuario())
                .urlImagen(dto.getUrlImagen())
                .build();
    }

    private ProductoDonadoDTO convertToDonadorDTO(Producto producto) {
        return ProductoDonadoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .descripcion(producto.getDescripcion())
                .estado(producto.getEstado())
                .idUsuario(producto.getIdUsuario())
                .urlImagen(producto.getUrlImagen())
                .build();
    }

    private ProductoDTO convertToDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .descripcion(producto.getDescripcion())
                .estado(producto.getEstado())
                .idUsuario(producto.getIdUsuario())
                .urlImagen(producto.getUrlImagen())
                .build();
    }

    private Producto convertToEntity(ProductoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .categoria(dto.getCategoria())
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .idUsuario(dto.getIdUsuario())
                .urlImagen(dto.getUrlImagen())
                .build();
    }
}
