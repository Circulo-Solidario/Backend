package com.backend.Backend.services;

import com.backend.Backend.dtos.solicitud.SolicitudDTO;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Solicitud;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.EstadoSolicitud;
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

    public Solicitud save(Solicitud solicitud) {
        Producto productoSolicitado = solicitud.getProducto();

        if (productoSolicitado.getEstado() == EstadoProducto.DISPONIBLE) {
            productoSolicitado.setEstado(EstadoProducto.SOLICITADO);
        }
        if (productoSolicitado.getSolicitantes() == null) {
            productoSolicitado.setSolicitantes(new ArrayList<>());
        }
        if (!productoSolicitado.getSolicitantes().contains(solicitud.getDeUsuario())) {
            productoSolicitado.getSolicitantes().add(solicitud.getDeUsuario());
        }
        productoRepository.save(productoSolicitado);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud cambiarEstado(Long id, EstadoSolicitud estado) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));
        solicitud.setEstado(estado);
        return solicitudRepository.save(solicitud);
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
            productoRepository.save(producto);
        }
    }
}
