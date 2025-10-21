package com.backend.Backend.services;

import com.backend.Backend.models.Producto;
import com.backend.Backend.repositories.ProductoRepository;
import com.backend.Backend.repositories.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;

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

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElse(null);
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
