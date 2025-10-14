package com.backend.Backend.services;


import com.backend.Backend.models.Mensaje;
import com.backend.Backend.repositories.MensajesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajesService {
    private final MensajesRepository messageRepository;

    public MensajesService(MensajesRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mensaje saveMessage(Mensaje message) {
        return messageRepository.save(message);
    }

    public List<Mensaje> getRoomMessages(String room) {
        return messageRepository.findByRoomOrderByTimestampAsc(room);
    }

    public void deleteAllMessagesForRoom(String room) {
        messageRepository.deleteByRoom(room);
    }
}
