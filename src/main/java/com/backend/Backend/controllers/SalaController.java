package com.backend.Backend.controllers;

import com.backend.Backend.dtos.sala.SalaResponseDTO;
import com.backend.Backend.mappers.SalaMapper;
import com.backend.Backend.models.Sala;
import com.backend.Backend.services.SalasService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/salas")
@CrossOrigin
@AllArgsConstructor
public class SalaController {
    private final SalasService salasService;
    private final SalaMapper salaMapper;

    @CrossOrigin
    @GetMapping("/solicitud/{id}")
    public ResponseEntity<SalaResponseDTO> findSalaBySolicitud(@RequestParam Long id) {
        Optional<Sala> sala = salasService.findSalaBySolicitudId(id);
        return sala.map(value -> ResponseEntity.ok(salaMapper.entityToSalaResponseDTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @GetMapping("/donador/{id}")
    public ResponseEntity<List<SalaResponseDTO>> findSalasByDondador(@PathVariable Long id) {
        List<Sala> salas = salasService.findSalaByDondadorId(id);
        return ResponseEntity.ok(salas.stream().map(salaMapper::entityToSalaResponseDTO).toList());
    }

    @CrossOrigin
    @GetMapping("/solicitante/{id}")
    public ResponseEntity<List<SalaResponseDTO>> findSalasBySolicitante(@PathVariable Long id){
        List<Sala> salas = salasService.findSalaBySolicitanteId(id);
        return ResponseEntity.ok(salas.stream().map(salaMapper::entityToSalaResponseDTO).toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> findSalaById(@PathVariable Long id) {
        Optional<Sala> sala = salasService.findSalaById(id);
        return sala.map(value -> ResponseEntity.ok(salaMapper.entityToSalaResponseDTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
