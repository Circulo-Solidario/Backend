package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizacionProyectosMetricsResponse {
    private Long organizacionId;
    private long totalProyectos;
    private double totalRecaudado;
    private double promedioAvancePorcentaje;
    private long proyectosActivos;
    private long proyectosCancelados;
    private long proyectosFinalizadosExitosos;
    private long proyectosFinalizadosNoExitosos;
    private List<OrganizacionProyectoResumen> topProyectosPorRecaudacion;
}
