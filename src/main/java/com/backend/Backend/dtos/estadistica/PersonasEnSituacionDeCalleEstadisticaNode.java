package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonasEnSituacionDeCalleEstadisticaNode {
    private int cantidadPuntosRegistrados;
    private int cantidadPuntosAyudados;
    private int ordenUsuario;
}
