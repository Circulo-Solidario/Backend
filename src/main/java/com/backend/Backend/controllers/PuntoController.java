package com.backend.Backend.controllers;

import com.backend.Backend.dtos.punto.EstadoRequest;
import com.backend.Backend.dtos.punto.PuntoRequest;
import com.backend.Backend.dtos.punto.PuntoResponse;
import com.backend.Backend.mappers.PuntoMapper;
import com.backend.Backend.models.Punto;
import com.backend.Backend.repositories.PuntoRepository;
import com.backend.Backend.services.PuntoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PuntoController {
    private final PuntoService puntoService;
    private final PuntoMapper puntoMapper;

    @GetMapping
    public ResponseEntity<List<PuntoResponse>> obtenerTodos() {
        return ResponseEntity.ok(puntoService.findAll().stream()
                .map(puntoMapper::entityToPuntoResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PuntoRequest dto) {
        try {
            Punto nuevoPunto = puntoService.saveNewPunto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(puntoMapper.entityToPuntoResponse(nuevoPunto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PuntoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody EstadoRequest request) {
        try {
            Punto puntoActualizado = puntoService.actualizarEstado(id, request);
            return ResponseEntity.ok(puntoMapper.entityToPuntoResponse(puntoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPunto(@PathVariable Long id) {
        try {
            puntoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

