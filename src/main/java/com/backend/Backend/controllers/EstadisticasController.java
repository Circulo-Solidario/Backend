package com.backend.Backend.controllers;

import com.backend.Backend.dtos.estadistica.EstadisticasPersonalesResponse;
import com.backend.Backend.services.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @GetMapping("/personal/{id}")
    public ResponseEntity<EstadisticasPersonalesResponse> obtenerEstadisticasPersonales(@PathVariable Long id) {
        EstadisticasPersonalesResponse response = estadisticasService.obtenerEstadisticasPersonales(id);
        return ResponseEntity.ok(response);
    }
}
