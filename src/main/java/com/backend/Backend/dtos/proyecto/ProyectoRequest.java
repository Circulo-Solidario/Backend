package com.backend.Backend.dtos.proyecto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProyectoRequest {
    private String nombre;
    private String descripcion;
    private Long idOrganizacion;
    private Date fechaFin;
    private Double objetivo;
    private List<String> imagenes;
}
