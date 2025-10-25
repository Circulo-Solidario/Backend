package com.backend.Backend.dtos.solicitud;

import com.backend.Backend.models.enums.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoDTO {
    private EstadoSolicitud estado;
}
