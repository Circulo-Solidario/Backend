package com.backend.Backend.mappers;

import com.backend.Backend.dtos.SolicitudDTO;
import com.backend.Backend.models.Solicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudMapper {
    public SolicitudDTO entityToDTO(Solicitud solicitud) {
        return SolicitudDTO.builder()
                .id(solicitud.getId())
                .aUsuario(solicitud.getAUsuario().getId())
                .deUsuario(solicitud.getDeUsuario().getId())
                .idProducto(solicitud.getProducto().getId())
                .build();
    }
}
