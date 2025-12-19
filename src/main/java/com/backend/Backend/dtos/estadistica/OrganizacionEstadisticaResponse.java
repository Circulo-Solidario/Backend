package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO para respuesta de estadísticas completas de una organización
 * Incluye métricas de proyectos, donaciones y donadores
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizacionEstadisticaResponse {
    private Long organizacionId;
    private String nombreOrganizacion;

    // Métricas de Proyectos
    private ProyectosMetrics proyectos;

    // Métricas de Donaciones
    private DonacionesMetrics donaciones;

    // Métricas de Donadores
    private DonadoresMetrics donadores;

    // Datos temporales (últimos 6 meses)
    private Map<String, Double> recaudacionPorMes;
    private Map<String, Long> donacionesPorMes;

    /**
     * Métricas relacionadas con proyectos
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProyectosMetrics {
        private long totalProyectos;
        private long proyectosActivos;
        private long proyectosCancelados;
        private long proyectosFinalizadosExitosos;
        private long proyectosFinalizadosNoExitosos;
        private double totalRecaudado;
        private double totalObjetivo;
        private double promedioAvancePorcentaje;
        private double tasaExito; // % de proyectos finalizados exitosamente
        private List<ProyectoResumen> topProyectosPorRecaudacion;
        private List<ProyectoResumen> proyectosRecientes;
    }

    /**
     * Métricas relacionadas con donaciones
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonacionesMetrics {
        private long totalDonaciones;
        private double montoTotalRecaudado;
        private double donacionPromedio;
        private double donacionMasAlta;
        private double donacionMasBaja;
        private double recaudacionUltimoMes;
        private double recaudacionMesAnterior;
        private double tasaCrecimiento; // % crecimiento mes actual vs anterior
    }

    /**
     * Métricas relacionadas con donadores
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonadoresMetrics {
        private long totalDonadoresUnicos;
        private long donadoresRecurrentes; // donaron más de una vez
        private double promedioRetencion; // % de donadores que vuelven a donar
        private List<DonadorResumen> topDonadores;
        private Map<String, Long> donacionesPorRango; // 0-100, 100-500, 500-1000, 1000+
    }

    /**
     * Resumen de un proyecto
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProyectoResumen {
        private Long id;
        private String nombre;
        private Double recaudado;
        private Double objetivo;
        private double porcentajeAvance;
        private String estado;
        private long totalDonaciones;
    }

    /**
     * Resumen de un donador
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonadorResumen {
        private Long id;
        private String nombre;
        private double montoTotal;
        private long cantidadDonaciones;
    }
}