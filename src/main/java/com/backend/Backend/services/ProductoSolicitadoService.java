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
import com.backend.Backend.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

    Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (producto.getSolicitantes() == null) {
            producto.setSolicitantes(new java.util.ArrayList<>());
        }
        if (producto.getSolicitantes().stream().noneMatch(u -> u.getId().equals(usuario.getId()))) {
            producto.getSolicitantes().add(usuario);
        }

        if (producto.getEstado() == EstadoProducto.DISPONIBLE) {
            producto.setEstado(EstadoProducto.SOLICITADO);
        }
        productoRepository.save(producto);

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
        ProductoSolicitado ps = productoSolicitadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductoSolicitado no encontrado"));

        Producto producto = ps.getProducto();

        productoSolicitadoRepository.deleteById(id);

        boolean noTieneSolicitantes = producto.getSolicitantes() == null || producto.getSolicitantes().isEmpty();
        if (noTieneSolicitantes && producto.getEstado() == EstadoProducto.SOLICITADO) {
            producto.setEstado(EstadoProducto.DISPONIBLE);
        }

        productoRepository.save(producto);
    }
}
