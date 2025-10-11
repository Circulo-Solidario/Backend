package com.backend.Backend.services;

import com.backend.Backend.dtos.ProductoSolicitadoDTO;
import com.backend.Backend.models.EstadoProducto;
import com.backend.Backend.models.Producto;
import com.backend.Backend.models.ProductoSolicitado;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.ProductoRepository;
import com.backend.Backend.repositories.ProductoSolicitadoRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoSolicitadoService {
    @Autowired
    private ProductoSolicitadoRepository productoSolicitadoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ProductoSolicitado> findAll() {
        return productoSolicitadoRepository.findAll();
    }

    public Optional<ProductoSolicitado> findById(Long id) {
        return productoSolicitadoRepository.findById(id);
    }

    public ProductoSolicitado save(ProductoSolicitadoDTO dto) {
        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ProductoSolicitado productoSolicitado = new ProductoSolicitado();
        productoSolicitado.setProducto(producto);
        productoSolicitado.setUsuario(usuario);
        productoSolicitado.setCategoria(producto.getCategoria());
        productoSolicitado.setNombre(producto.getNombre());
        productoSolicitado.setDescripcion(producto.getDescripcion());
        productoSolicitado.setEstado(EstadoProducto.SOLICITADO);

        return productoSolicitadoRepository.save(productoSolicitado);
    }

    public void deleteById(Long id) {
        productoSolicitadoRepository.deleteById(id);
    }
}
