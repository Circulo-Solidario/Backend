package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaResponse {

    // Usuarios
    private long totalUsuarios;
    private long usuariosActivos;
    private long usuariosInactivos;
    private long totalOrganizaciones;

    // Productos
    private long totalProductos;
    private long productosDisponibles;
    private long productosSolicitados;
    private long productosReservados;
    private long productosEntregados;

    // Proyectos
    private long totalProyectos;
    private long proyectosActivos;
    private long proyectosCancelados;
    private long proyectosFinalizadosExitosos;
    private long proyectosFinalizadosNoExitosos;
    private double totalRecaudado;

    // Puntos
    private long totalPuntos;
    private long puntosPendientes;
    private long puntosAtendidos;
}