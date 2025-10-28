package com.backend.Backend.controllers;

import com.backend.Backend.dtos.mensaje.MensajeRequest;
import com.backend.Backend.dtos.mensaje.MensajeResponse;
import com.backend.Backend.mappers.MensajeMapper;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Mensaje;
import com.backend.Backend.models.Sala;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.services.MensajesService;
import com.backend.Backend.services.SalasService;
import com.backend.Backend.services.UsuarioService;
import com.pusher.rest.Pusher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin
@RequiredArgsConstructor
public class MensajeController {
    private final Pusher pusher;
    private final MensajesService mensajesService;
    private final UsuarioService usuarioService;
    private final SalasService salasService;
    private final MensajeMapper mensajeMapper;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/enviar")
    public ResponseEntity<MensajeResponse> sendMessage(@RequestBody MensajeRequest mensaje) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(mensaje.getIdUsuario());
        Optional<Sala> sala =  salasService.findSalaById(mensaje.getIdSala());

        if (mensaje.getMensaje() == null
                || mensaje.getMensaje().trim().isEmpty()
                || usuario.isEmpty()
                || sala.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        Mensaje nuevoMensaje = new Mensaje();
        nuevoMensaje.setMensaje(mensaje.getMensaje());
        nuevoMensaje.setUsuario(usuario.get());
        nuevoMensaje.setSala(sala.get());
        nuevoMensaje.setFechaMensaje(Date.from(Instant.now()));

        Mensaje mensajeGuardado = mensajesService.saveMessage(nuevoMensaje);

        Map<String, Object> mensajeData = new HashMap<>();
        mensajeData.put("id", mensajeGuardado.getId());
        mensajeData.put("mensaje", mensajeGuardado.getMensaje());
        mensajeData.put("usuario", usuarioMapper.mapEntityToUsuarioSimple(mensajeGuardado.getUsuario()));
        mensajeData.put("sala", mensajeGuardado.getSala().getNombreSala());
        mensajeData.put("fechaMensaje", mensajeGuardado.getFechaMensaje());

        pusher.trigger(
                mensajeGuardado.getSala().getNombreSala(),
                "new-message",
                mensajeData
        );

        return ResponseEntity.ok(mensajeMapper.entityToResponseDto(mensajeGuardado));
    }

    @CrossOrigin
    @GetMapping("/sala/{id}")
    public ResponseEntity<List<MensajeResponse>> findAllBySala(@PathVariable Long id) {
        List<Mensaje> mensajes = mensajesService.getAllMessagesBySala(id);
        return ResponseEntity.ok(mensajes.stream().map(mensajeMapper::entityToResponseDto).toList());
    }
}

