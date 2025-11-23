package com.backend.Backend.mappers;

import com.backend.Backend.dtos.solicitud.SolicitudResponseDTO;
import com.backend.Backend.models.Solicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudMapper {
    private final UsuarioMapper usuarioMapper;
    private final ProductoMapper productoMapper;

    public SolicitudResponseDTO entityToSolicitudResponseDTO(Solicitud solicitud) {
        return SolicitudResponseDTO.builder()
                .id(solicitud.getId())
                .donador(usuarioMapper.mapEntityToUsuarioSimple(solicitud.getDonador()))
                .solicitante(usuarioMapper.mapEntityToUsuarioSimple(solicitud.getSolicitante()))
                .producto(productoMapper.entityToProductoSinSolicitudes(solicitud.getProducto()))
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .mensaje(solicitud.getMensaje())
                .estadoSolicitud(solicitud.getEstado())
                .build();
    }
}
