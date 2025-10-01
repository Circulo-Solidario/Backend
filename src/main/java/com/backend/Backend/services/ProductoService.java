package com.backend.Backend.services;

import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Roles;
import com.backend.Backend.repositories.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto planta) {
        return productoRepository.save(planta);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> findAllByCategoria(String categoria) {
        return productoRepository.findListaDeProductosByCategoria(categoria);
    }
}
