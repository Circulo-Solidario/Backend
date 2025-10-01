package com.backend.Backend.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String estado;
    private Long idUsuario;
    private String urlImagen;
}
