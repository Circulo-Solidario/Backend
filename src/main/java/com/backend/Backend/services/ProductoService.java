package com.backend.Backend.services;

import com.backend.Backend.models.Producto;
import com.backend.Backend.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public Page<Producto> findByFilters(String nombre, Long categoriaId, Pageable pageable) {
        if (nombre != null && categoriaId != null) {
            return productoRepository.findByNombreContainingIgnoreCaseAndCategoriaId(nombre, categoriaId, pageable);
        } else if (nombre != null) {
            return productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        } else if (categoriaId != null) {
            return productoRepository.findByCategoriaId(categoriaId, pageable);
        } else {
            return productoRepository.findAll(pageable);
        }
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> findAllByCategoria(Long categoriaId) {
        return productoRepository.findListaDeProductosByCategoria(categoriaId);
    }

    public List<Producto> findProductosByUsuarioDonador(Long usuarioId) {
        return productoRepository.findAllByUsuarioId(usuarioId);
    }
}
