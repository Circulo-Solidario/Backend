package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyectosSolidariosEstadisticaNode {
    private long cantidadPublicados;
    private double totalRecaudado;
    private Map<String, Long> cantidadPorEstado; // ACTIVO, CANCELADO, FINALIZADO_EXITOSO, FINALIZADO_NO_EXITOSO
    private List<UsuarioDonacionesResumen> topUsuariosConMasDonaciones;
    private Map<String, Double> recaudoPorDia; // día -> monto recaudado
}

