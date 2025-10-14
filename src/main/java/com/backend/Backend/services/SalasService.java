package com.backend.Backend.services;

import com.backend.Backend.models.Sala;
import com.backend.Backend.repositories.SalasRepository;
import org.springframework.stereotype.Service;

@Service
public class SalasService {
    private final SalasRepository roomsRepository;

    public SalasService(SalasRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    public String searchRoom(Long user1, Long user2) {
        return roomsRepository.getRoomFromUsers(user1, user2);
    }

    public String createRoom(Long user1, Long user2) {
        return roomsRepository.save(new Sala(user1, user2)).getRoomName();
    }
}
