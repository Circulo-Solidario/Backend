package com.backend.Backend.controllers;

import com.backend.Backend.models.Punto;
import com.backend.Backend.repositories.PuntoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Punto crear(@RequestBody Punto punto) {
        punto.setEstado("pendiente");
        return repo.save(punto);
    }
}
