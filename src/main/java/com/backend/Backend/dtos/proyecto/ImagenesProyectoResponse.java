package com.backend.Backend.dtos.proyecto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagenesProyectoResponse {
    private Long id;
    private String urlImagen;
}
