package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalEstadisticaResponse {

    private long proyectosCreados;
    private long proyectosActivos;
    private long proyectosFinalizados;
    private double totalRecaudadoPorProyectos;

    private long productosCreados;
    private long productosDisponibles;
    private long productosSolicitadosPorUsuario;

    private long notificacionesNoLeidas;
    private long mensajesEnviados;
}
