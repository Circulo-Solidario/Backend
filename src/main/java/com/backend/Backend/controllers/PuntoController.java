package com.backend.Backend.controllers;

import com.backend.Backend.dtos.punto.EstadoRequest;
import com.backend.Backend.dtos.punto.PuntoRequest;
import com.backend.Backend.models.Punto;
import com.backend.Backend.repositories.PuntoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puntos")
@CrossOrigin(origins = "*")
public class PuntoController {

    private final PuntoRepository repo;

    public PuntoController(PuntoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Punto> obtenerTodos() {
        return repo.findAll();
    }

    @PostMapping
    public Punto crear(@RequestBody PuntoRequest dto) {
        Punto punto = Punto.builder()
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .descripcion(dto.getDescripcion())
                .estado("pendiente")
                .build();

        return repo.save(punto);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Punto> actualizarEstado(
            @PathVariable Long id,
            @RequestBody EstadoRequest request) {

        Punto punto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto no encontrado"));

        punto.setEstado(request.getEstado());
        repo.save(punto);

        return ResponseEntity.ok(punto);
    }
}
