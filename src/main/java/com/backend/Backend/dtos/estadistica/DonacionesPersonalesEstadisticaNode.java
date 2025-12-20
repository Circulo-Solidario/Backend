package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonacionesPersonalesEstadisticaNode {
    private double cantidadDonada;
    private double donacionMasAlta;
    private Map<String, Object> organizacionMasAyudada;
    private List<DonacionDetalle> donaciones;
}
