package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductosPersonalesEstadisticaNode {
    private int cantidadPublicados;
    private int cantidadDonados;
    private Map<String, Long> cantidadPorEstado;
    private int cantidadSolicitados;
    private int cantidadRecibidos;
    private Map<String, Long> cantidadPorCategoriaSolicitada;
}
