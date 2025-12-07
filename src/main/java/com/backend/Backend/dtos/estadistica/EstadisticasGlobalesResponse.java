package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasGlobalesResponse {
    private UsuariosEstadisticaNode usuarios;
    private ProductosEstadisticaNode productos;
    private ProyectosSolidariosEstadisticaNode proyectosSolidarios;
    private PuntosEstadisticaNode puntos;
}

