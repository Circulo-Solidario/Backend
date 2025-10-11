package com.backend.Backend.controllers;

import com.backend.Backend.dtos.CategoriaDTO;
import com.backend.Backend.dtos.ProductoDTO;
import com.backend.Backend.dtos.ProductoDonadoDTO;
import com.backend.Backend.models.Categoria;
import com.backend.Backend.models.Producto;
import com.backend.Backend.repositories.CategoriaRepository;
import com.backend.Backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private CategoriaRepository categoriaRepository;

    @CrossOrigin
    @GetMapping
    public Page<ProductoDTO> getAllProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.findByFilters(nombre, categoriaId, pageable);

        return productos.map(this::convertToDTO);
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
    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoDTO> getProductosByCategoria(@PathVariable Long categoriaId) {
        return productoService.findAllByCategoria(categoriaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/usuario-donador/{usuarioId}")
    public ResponseEntity<List<ProductoDTO>> getProductosByUsuarioDonador(@PathVariable Long usuarioId) {
        List<Producto> productos = productoService.findProductosByUsuarioDonador(usuarioId);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProductoDTO> productosDTO = productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productosDTO);
    }

    private Producto convertToEntity(ProductoDonadoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .categoria(convertCategoriaFromDTO(dto.getCategoria()))
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
                .categoria(convertCategoriaToDTO(producto.getCategoria()))
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
                .categoria(convertCategoriaToDTO(producto.getCategoria()))
                .descripcion(producto.getDescripcion())
                .estado(producto.getEstado())
                .idUsuario(producto.getIdUsuario())
                .urlImagen(producto.getUrlImagen())
                .build();
    }

    private Producto convertToEntity(ProductoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .categoria(convertCategoriaFromDTO(dto.getCategoria()))
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .idUsuario(dto.getIdUsuario())
                .urlImagen(dto.getUrlImagen())
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
