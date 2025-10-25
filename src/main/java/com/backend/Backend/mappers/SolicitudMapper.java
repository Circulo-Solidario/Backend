package com.backend.Backend.mappers;

import com.backend.Backend.dtos.producto.ProductoSinLolicitudesDTO;
import com.backend.Backend.dtos.solicitud.SolicitudResponseDTO;
import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.Solicitud;
import com.backend.Backend.models.enums.EstadoSolicitud;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class SolicitudMapper {
    private final UsuarioMapper usuarioMapper;
    private final ProductoMapper productoMapper;

    public SolicitudResponseDTO entityToSolicitudResponseDTO(Solicitud solicitud) {
        return SolicitudResponseDTO.builder()
                .id(solicitud.getId())
                .aUsuario(usuarioMapper.mapEntityToUsuarioSimple(solicitud.getAusuario()))
                .deUsuario(usuarioMapper.mapEntityToUsuarioSimple(solicitud.getDeUsuario()))
                .producto(productoMapper.entityToProductoSinSolicitudes(solicitud.getProducto()))
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .mensaje(solicitud.getMensaje())
                .estadoSolicitud(solicitud.getEstado())
                .build();
    }
}
