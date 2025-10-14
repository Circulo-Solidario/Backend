package com.backend.Backend.controllers;

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
    public ResponseEntity<Sala> findRoom(@RequestParam(required = true) Long user1, @RequestParam(required = true) Long user2) {
        try {
            Sala room = new Sala();
            room.setRoomName(roomsService.searchRoom(user1, user2));
            if (room.getRoomName() == null) {
                room.setRoomName(roomsService.createRoom(user1, user2));
            }
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
