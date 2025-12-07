package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductosEstadisticaNode {
    private long cantidadPublicados;
    private Map<String, Long> cantidadPorEstado; // DISPONIBLE, SOLICITADO, RESERVADO, ENTREGADO
    private Map<String, Long> cantidadPorCategoria;
    private List<UsuarioProductosEntregadosResumen> topUsuariosConMasEntregados;
}

