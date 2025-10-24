package com.backend.Backend.mappers;

import com.backend.Backend.dtos.mensaje.MensajeRequest;
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

    public Mensaje mensajeRequestToEntity(MensajeRequest mensajeRequest){
        return Mensaje.builder()
                .mensaje(mensajeRequest.getMensaje())
                .fechaMensaje(Date.from(Instant.now()))
                .build();

    }
}
