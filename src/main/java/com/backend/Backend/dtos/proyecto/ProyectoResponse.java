package com.backend.Backend.dtos.proyecto;

import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private UsuarioSimpleDTO organizacion;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fechaInicio;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fechaFin;
    private Double objetivo;
    private Double recaudado;
    private EstadoProyecto estado;
    private List<ImagenesProyectoResponse> imagenes;
}
