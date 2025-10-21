package com.backend.Backend.services;


import com.backend.Backend.models.Mensaje;
import com.backend.Backend.repositories.MensajesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensajesService {
    private final MensajesRepository messageRepository;

    public Mensaje saveMessage(Mensaje message) {
        return messageRepository.save(message);
    }

    public List<Mensaje> getRoomMessages(String nombreSala) {
        return messageRepository.findAllBySala_NombreSala(nombreSala).stream()
                .sorted(Comparator.comparing(Mensaje::getFechaMensaje).reversed())
                .collect(Collectors.toList());
    }
}
