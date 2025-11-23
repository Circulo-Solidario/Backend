package com.backend.Backend.mappers;

import com.backend.Backend.dtos.proyecto.ImagenesProyectoResponse;
import com.backend.Backend.dtos.proyecto.ProyectoResponse;
import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.ImagenesProyecto;
import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProyectoMapper {
    private final UsuarioMapper usuarioMapper;

    private ImagenesProyectoResponse imagenesEntityToDto(ImagenesProyecto imagenesProyecto) {
        return ImagenesProyectoResponse.builder()
                .id(imagenesProyecto.getId())
                .urlImagen(imagenesProyecto.getUrlImagen())
                .build();
    }

    public ProyectoResponse entityToResponse(Proyecto proyecto) {
        return ProyectoResponse.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .descripcion(proyecto.getDescripcion())
                .organizacion(usuarioMapper.mapEntityToUsuarioSimple(proyecto.getOrganizacion()))
                .fechaInicio(proyecto.getFechaInicio())
                .fechaFin(proyecto.getFechaFin())
                .objetivo(proyecto.getObjetivo())
                .recaudado(proyecto.getRecaudado())
                .estado(proyecto.getEstado())
                .imagenes(proyecto.getImagenes().stream().map(this::imagenesEntityToDto).toList())
                .build();
    }
}
