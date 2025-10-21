package com.backend.Backend.mappers;

import com.backend.Backend.dtos.MensajeRequest;
import com.backend.Backend.models.Mensaje;
import com.backend.Backend.services.SalasService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class MensajeMapper {
    private final SalasService salasService;
    private final UsuarioService usuarioService;

    public Mensaje mensajeRequestToEntity(MensajeRequest mensajeRequest){
        return Mensaje.builder()
                .mensaje(mensajeRequest.getMensaje())
                .sala(salasService.findRoomByName(mensajeRequest.getSala()))
                .usuario(usuarioService.getUsuarioById(mensajeRequest.getIdUsuario()))
                .fechaMensaje(Date.from(Instant.now()))
                .build();

    }
}
