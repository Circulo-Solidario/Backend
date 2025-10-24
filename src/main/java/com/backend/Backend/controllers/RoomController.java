package com.backend.Backend.controllers;

import com.backend.Backend.dtos.sala.CreateRoomDTO;
import com.backend.Backend.models.Sala;
import com.backend.Backend.services.SalasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salas")
@CrossOrigin
public class RoomController {
    private final SalasService roomsService;

    public RoomController(SalasService roomsService) {
        this.roomsService = roomsService;
    }

    @GetMapping("/buscarSala")
    public ResponseEntity<Sala> findRoom(@RequestParam Long user1, @RequestParam Long user2,
                                         @RequestParam Long productId) {
        try {
            Sala room = new Sala();
            room.setNombreSala(roomsService.searchRoom(user1, user2, productId));
            if (room.getNombreSala() == null) {
                room.setNombreSala(roomsService.createRoom(user1, user2, productId));
            }
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/crearSala")
    public ResponseEntity<Boolean> createRoom(@RequestBody CreateRoomDTO room) {
        return ResponseEntity.ok(true);
    }
}
