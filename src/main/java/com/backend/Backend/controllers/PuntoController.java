package com.backend.Backend.controllers;

import com.backend.Backend.dtos.punto.ActualizarPuntoRequest;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPunto(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body("Punto no encontrado");
        }

        repo.deleteById(id);

        return ResponseEntity.ok("Punto eliminado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPunto(@PathVariable Long id, @RequestBody ActualizarPuntoRequest request) {

        Punto punto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto no encontrado"));

        if (request.getLatitud() != null) punto.setLatitud(request.getLatitud());
        if (request.getLongitud() != null) punto.setLongitud(request.getLongitud());
        if (request.getDescripcion() != null) punto.setDescripcion(request.getDescripcion());
        if (request.getEstado() != null) punto.setEstado(request.getEstado());

        repo.save(punto);

        return ResponseEntity.ok(punto);
    }
}
