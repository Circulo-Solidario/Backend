package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasPersonalesResponse {
    private ProductosPersonalesEstadisticaNode productos;
    private PersonasEnSituacionDeCalleEstadisticaNode personasEnSituacionDeCalle;
    private DonacionesPersonalesEstadisticaNode donaciones;
}
