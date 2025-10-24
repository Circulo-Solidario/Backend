package com.backend.Backend.mappers;

import com.backend.Backend.dtos.categoria.CategoriaDTO;
import com.backend.Backend.models.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria convertToEntity(CategoriaDTO dto) {
        return Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }
}
