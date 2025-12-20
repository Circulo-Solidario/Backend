package com.backend.Backend.mappers;

import com.backend.Backend.dtos.punto.PuntoResponse;
import com.backend.Backend.models.Punto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PuntoMapper {
    private final UsuarioMapper usuarioMapper;

    public PuntoResponse entityToPuntoResponse(Punto punto) {
        return PuntoResponse.builder()
                .id(punto.getId())
                .latitud(punto.getLatitud())
                .longitud(punto.getLongitud())
                .descripcion(punto.getDescripcion())
                .estado(punto.getEstado())
                .usuarioRegistro(usuarioMapper.mapEntityToUsuarioSimple(punto.getUsuarioRegistro()))
                .usuarioAyudo(punto.getUsuarioAyudo() != null ? usuarioMapper.mapEntityToUsuarioSimple(punto.getUsuarioAyudo()) : null)
                .build();
    }
}
