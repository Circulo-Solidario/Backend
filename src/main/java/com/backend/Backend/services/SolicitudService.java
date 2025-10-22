package com.backend.Backend.services;

import com.backend.Backend.dtos.SolicitudDTO;
import com.backend.Backend.models.EstadoProducto;
import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Solicitud;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.ProductoRepository;
import com.backend.Backend.repositories.SolicitudRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.backend.Backend.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> findSolicitudesDeUsuario(Long deUsuario) {
        return solicitudRepository.findAllByDeUsuario_Id(deUsuario);
    }

    public List<Solicitud> findSolicitudesAUsuario(Long aUsuario) {
        return solicitudRepository.findAllByAusuario_Id(aUsuario);
    }

    public Optional<Solicitud> findById(Long id) {
        return solicitudRepository.findById(id);
    }

    public Solicitud save(SolicitudDTO dto) {
        Producto producto = productoRepository.findById(dto.getIdProducto())
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Usuario aUsuario = usuarioRepository.findById(dto.getAUsuario())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Usuario deUsuario = usuarioRepository.findById(dto.getDeUsuario())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));


        if (producto.getEstado() == EstadoProducto.DISPONIBLE) {
            producto.setEstado(EstadoProducto.SOLICITADO);
        }

        if (producto.getSolicitantes() == null) {
            producto.setSolicitantes(new ArrayList<>());
        }
        if (!producto.getSolicitantes().contains(deUsuario)) {
            producto.getSolicitantes().add(deUsuario);
        }

        productoRepository.save(producto);

        Solicitud solicitud = new Solicitud();
        solicitud.setProducto(producto);
        solicitud.setDeUsuario(deUsuario);
        solicitud.setAusuario(aUsuario);
        solicitud.setAceptada(false);

        return solicitudRepository.save(solicitud);
    }

    public Solicitud update(Long id, Boolean aceptada) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        if (solicitud.isEmpty()) {
            throw new ResourceNotFoundException("Solicitud no encontrada");
        }
        solicitud.get().setAceptada(aceptada);
        return solicitudRepository.save(solicitud.get());
    }

    public void deleteById(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrado"));

        solicitudRepository.deleteById(id);

        Producto producto = solicitud.getProducto();
        List<Solicitud> solictudesProducto = solicitudRepository.findAllByProductoId(solicitud.getProducto().getId());

        boolean noTieneSolicitantes = solictudesProducto == null || solictudesProducto.isEmpty();
        if (noTieneSolicitantes && producto.getEstado() == EstadoProducto.SOLICITADO) {
            producto.setEstado(EstadoProducto.DISPONIBLE);
        }

        productoRepository.save(producto);
    }
}
