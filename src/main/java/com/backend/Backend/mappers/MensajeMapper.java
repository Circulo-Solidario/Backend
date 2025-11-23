package com.backend.Backend.mappers;

import com.backend.Backend.dtos.mensaje.MensajeResponse;
import com.backend.Backend.models.Mensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MensajeMapper {
    private final UsuarioMapper usuarioMapper;

    public MensajeResponse entityToResponseDto(Mensaje mensaje) {
        return MensajeResponse.builder()
                .id(mensaje.getId())
                .mensaje(mensaje.getMensaje())
                .usuario(usuarioMapper.mapEntityToUsuarioSimple(mensaje.getUsuario()))
                .nombreSala(mensaje.getSala().getNombreSala())
                .fechaMensaje(mensaje.getFechaMensaje())
                .build();
    }
}
