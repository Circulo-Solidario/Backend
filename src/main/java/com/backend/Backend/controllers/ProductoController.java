package com.backend.Backend.controllers;

import com.backend.Backend.dtos.CategoriaDTO;
import com.backend.Backend.dtos.ProductoDTO;
import com.backend.Backend.dtos.ProductoDonadoDTO;
import com.backend.Backend.mappers.ProductoMapper;
import com.backend.Backend.models.Categoria;
import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.repositories.CategoriaRepository;
import com.backend.Backend.services.ProductoService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioService usuarioService;
    private final ProductoMapper productoMapper;

    @CrossOrigin
    @GetMapping
    public Page<ProductoDTO> getAllProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.findByFilters(nombre, categoriaId, pageable);

        return productos.map(productoMapper::convertToDTO);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(productoMapper.convertToDTO(producto));
    }

    @CrossOrigin
    @PostMapping("/donador")
    public ResponseEntity<ProductoDonadoDTO> createProductoDonador(@RequestBody ProductoDonadoDTO dto) {
        Producto producto = productoMapper.convertToEntity(dto);
        Producto savedProducto = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoMapper.convertToDonadorDTO(savedProducto));
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        Producto existingProducto = productoService.findById(id);
        if (existingProducto == null) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = productoMapper.convertToEntity(dto);
        producto.setId(id);
        Producto updatedProducto = productoService.save(producto);
        return ResponseEntity.ok(productoMapper.convertToDTO(updatedProducto));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoDTO> getProductosByCategoria(@PathVariable Long categoriaId) {
        return productoService.findAllByCategoria(categoriaId).stream()
                .map(productoMapper::convertToDTO)
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
                .map(productoMapper::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productosDTO);
    }
}
