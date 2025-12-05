package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuntosEstadisticaNode {
    private long cantidadTotal;
    private Map<String, Long> cantidadPorEstado; // pendiente, atendido, etc
}

