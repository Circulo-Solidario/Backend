package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizacionProyectoResumen {
    private Long id;
    private String nombre;
    private Double recaudado;
    private Double objetivo;
    private double porcentajeAvance;
}
