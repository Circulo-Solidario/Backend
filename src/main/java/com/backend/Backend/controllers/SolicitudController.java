package com.backend.Backend.controllers;

import com.backend.Backend.dtos.solicitud.EstadoDTO;
import com.backend.Backend.dtos.solicitud.SolicitudDTO;
import com.backend.Backend.dtos.solicitud.SolicitudResponseDTO;
import com.backend.Backend.mappers.SolicitudMapper;
import com.backend.Backend.models.*;
import com.backend.Backend.models.enums.EstadoSolicitud;
import com.backend.Backend.services.ProductoService;
import com.backend.Backend.services.SolicitudService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {
    private final SolicitudService solicitudService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final SolicitudMapper solicitudMapper;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> getAllSolicitudes(@RequestParam(required = false) Long deUsuario, @RequestParam(required = false) Long aUsuario) {
        if (deUsuario != null && aUsuario != null) {
            return ResponseEntity.badRequest().build();
        }
        if(deUsuario != null){
            return ResponseEntity.ok(solicitudService.findSolicitudesDeUsuario(deUsuario).stream().map(solicitudMapper::entityToSolicitudResponseDTO).toList());
        }
        if(aUsuario != null){
            return ResponseEntity.ok(solicitudService.findSolicitudesAUsuario(aUsuario).stream().map(solicitudMapper::entityToSolicitudResponseDTO).toList());
        }
        return ResponseEntity.ok(solicitudService.findAll().stream().map(solicitudMapper::entityToSolicitudResponseDTO).toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> getSolicitudById(@PathVariable Long id) {
        Optional<Solicitud> solicitud = solicitudService.findById(id);
        return solicitud.map(value -> ResponseEntity.ok(solicitudMapper.entityToSolicitudResponseDTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Solicitud> crear(@RequestBody SolicitudDTO dto) {
        Optional<Producto> producto = productoService.findById(dto.getIdProducto());
        Optional<Usuario> deUsuario = usuarioService.getUsuarioById(dto.getDeUsuario());
        Optional<Usuario> aUsuario = usuarioService.getUsuarioById(dto.getAUsuario());
        if(producto.isEmpty() || deUsuario.isEmpty() || aUsuario.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Solicitud solicitud = new Solicitud();
        solicitud.setDeUsuario(deUsuario.get());
        solicitud.setAusuario(aUsuario.get());
        solicitud.setProducto(producto.get());
        solicitud.setFechaSolicitud(Date.from(Instant.now()));
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setMensaje(dto.getMensaje());
        Solicitud nuevaSolicitud = solicitudService.save(solicitud);
        return ResponseEntity.ok(nuevaSolicitud);
    }

    @CrossOrigin
    @PatchMapping("/cambiar-estado/{id}")
    public ResponseEntity<Solicitud> cambiarEstadoSolicitud(@PathVariable Long id, @RequestBody EstadoDTO estado) {
        Optional<Solicitud> solicitud = solicitudService.findById(id);
        if(solicitud.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Solicitud solicitudCambio = solicitudService.cambiarEstado(id, estado.getEstado());
        return ResponseEntity.ok(solicitudCambio);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        Optional<Solicitud> solicitud = solicitudService.findById(id);
        if(solicitud.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        solicitudService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
