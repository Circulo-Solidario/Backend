package com.backend.Backend.dtos.mensaje;

import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MensajeResponse {
    private Long id;
    private String mensaje;
    private UsuarioSimpleDTO usuario;
    private String nombreSala;
    @JsonFormat(pattern = "MMM dd, yyyy, h:mm:ss a", locale = "es_AR")
    private Date fechaMensaje;
}
