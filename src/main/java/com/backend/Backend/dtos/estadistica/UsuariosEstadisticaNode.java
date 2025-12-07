package com.backend.Backend.dtos.estadistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosEstadisticaNode {
    private long cantidadRegistrados;
    private Map<String, Long> cantidadPorTipo; // USUARIO, ORGANIZACION
    private Map<String, Long> usuariosUserPorRol; // donante, donatario, observador
}

