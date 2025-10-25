package com.backend.Backend.dtos.solicitud;

import com.backend.Backend.dtos.producto.ProductoSinLolicitudesDTO;
import com.backend.Backend.dtos.usuario.UsuarioDTO;
import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.enums.EstadoSolicitud;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudResponseDTO {
    private Long id;
    private UsuarioSimpleDTO deUsuario;
    private UsuarioSimpleDTO aUsuario;
    private ProductoSinLolicitudesDTO producto;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fechaSolicitud;
    private String mensaje;
    private EstadoSolicitud estadoSolicitud;
}
