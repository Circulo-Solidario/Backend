package com.backend.Backend.dtos.sala;

import com.backend.Backend.dtos.solicitud.SolicitudResponseDTO;
import com.backend.Backend.models.enums.EstadoSala;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaResponseDTO {
    private Long id;
    private SolicitudResponseDTO solicitud;
    private String nombreSala;
    private EstadoSala estado;
}
