package com.backend.Backend.dtos.sala;

import com.backend.Backend.models.enums.EstadoSala;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSalaDTO {
    private EstadoSala estado;
}
