package com.backend.Backend.controllers;

import com.backend.Backend.dtos.categoria.CategoriaDTO;
import com.backend.Backend.mappers.CategoriaMapper;
import com.backend.Backend.models.Categoria;
import com.backend.Backend.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAll();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getProductoById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@RequestBody CategoriaDTO dto) {
        Categoria categoria = categoriaMapper.convertToEntity(dto);
        Categoria savedCategoria = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoria);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCateogoria(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        Optional<Categoria> existingCategoria = categoriaService.findById(id);
        if (existingCategoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Categoria categoria = categoriaMapper.convertToEntity(dto);
        categoria.setId(id);
        Categoria updatedCategoria = categoriaService.save(categoria);
        return ResponseEntity.ok(updatedCategoria);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (categoriaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
