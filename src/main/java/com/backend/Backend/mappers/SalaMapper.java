package com.backend.Backend.mappers;

import com.backend.Backend.dtos.sala.SalaResponseDTO;
import com.backend.Backend.models.Sala;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalaMapper {
    private final SolicitudMapper solicitudMapper;

    public SalaResponseDTO entityToSalaResponseDTO(Sala sala) {
        return SalaResponseDTO.builder()
                .id(sala.getId())
                .solicitud(solicitudMapper.entityToSolicitudResponseDTO(sala.getSolicitud()))
                .nombreSala(sala.getNombreSala())
                .estado(sala.getEstado())
                .build();
    }
}
