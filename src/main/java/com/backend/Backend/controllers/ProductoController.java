package com.backend.Backend.controllers;

import com.backend.Backend.dtos.producto.PublicarProductoDTO;
import com.backend.Backend.models.Categoria;
import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.services.CategoriaService;
import com.backend.Backend.services.ProductoService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Page<Producto>> getAllProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId) {
        Optional<Categoria> categoria = categoriaService.findById(categoriaId);
        if(categoria.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.findByFilters(nombre, categoriaId, pageable);
        if(productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Producto> createProductoDonador(@RequestBody PublicarProductoDTO dto) {
        Optional<Categoria> categoria = categoriaService.findById(dto.getCategoriaId());
        Optional<Usuario> usuario = usuarioService.getUsuarioById(dto.getIdUsuario());
        if(categoria.isEmpty() || usuario.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Producto producto = new Producto();
        producto.setNombre(producto.getNombre());
        producto.setCategoria(categoria.get());
        producto.setDescripcion(dto.getDescripcion());
        producto.setUsuario(usuario.get());
        producto.setUrlImagen(dto.getUrlImagen());
        producto.setEstado(EstadoProducto.DISPONIBLE);
        Producto savedProducto = productoService.save(producto);
        return ResponseEntity.ok(savedProducto);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody PublicarProductoDTO dto) {
        Optional<Producto> existingProducto = productoService.findById(id);
        Optional<Categoria> categoria = categoriaService.findById(dto.getCategoriaId());
        Optional<Usuario> usuario = usuarioService.getUsuarioById(dto.getIdUsuario());
        if (existingProducto.isEmpty() || categoria.isEmpty() || usuario.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Producto productoActual = existingProducto.get();
        productoActual.setNombre(productoActual.getNombre());
        productoActual.setCategoria(categoria.get());
        productoActual.setDescripcion(dto.getDescripcion());
        productoActual.setUsuario(usuario.get());
        productoActual.setUrlImagen(dto.getUrlImagen());
        Producto updatedProducto = productoService.save(productoActual);
        return ResponseEntity.ok(updatedProducto);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoService.findById(id);
        if (producto.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @GetMapping("/usuario-donador/{usuarioId}")
    public ResponseEntity<List<Producto>> getProductosByUsuarioDonador(@PathVariable Long usuarioId) {
        List<Producto> productos = productoService.findProductosByUsuarioDonador(usuarioId);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }
}
