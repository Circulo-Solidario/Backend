package com.backend.Backend.controllers;

import com.backend.Backend.dtos.SolicitudDTO;
import com.backend.Backend.mappers.SolicitudMapper;
import com.backend.Backend.models.*;
import com.backend.Backend.services.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productosSolicitados")
@RequiredArgsConstructor
public class SolicitudController {
    private final SolicitudService solicitudService;
    private final SolicitudMapper solicitudMapper;

    @CrossOrigin
    @GetMapping
    public List<Solicitud> getAllSolicitudes() {
        return solicitudService.findAll();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Solicitud getSolicitudById(@PathVariable Long id) {
        Optional<Solicitud> productoSolicitado = solicitudService.findById(id);
        return productoSolicitado.orElse(null);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Solicitud> crear(@RequestBody SolicitudDTO dto) {
        Solicitud nuevo = solicitudService.save(dto);
        return ResponseEntity.ok(nuevo);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> updateSolicitud(@PathVariable Long id, @RequestBody SolicitudDTO SolicitudDTO) {
        SolicitudDTO.setId(id);
        return ResponseEntity.ok(solicitudService.save(SolicitudDTO));
    }

    @CrossOrigin
    @PatchMapping("/{id}")
    public ResponseEntity<Solicitud> aceptarRechazarSolicitud(@PathVariable Long id, @RequestBody Boolean aceptada) {
        Solicitud solicitud = solicitudService.update(id, aceptada);
        if(solicitud == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(solicitud);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        solicitudService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
