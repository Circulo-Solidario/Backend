package com.backend.Backend.services;

import com.backend.Backend.models.Mensaje;
import com.backend.Backend.repositories.MensajesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajesService {
    private final MensajesRepository messageRepository;

    public Mensaje saveMessage(Mensaje message) {
        return messageRepository.save(message);
    }

    public List<Mensaje> getAllMessagesBySala(Long salaId) {
        return messageRepository.findAllBySalaIdOrderByFechaMensajeAsc(salaId);
    }
}
