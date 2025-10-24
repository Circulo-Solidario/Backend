package com.backend.Backend.controllers;

import com.backend.Backend.dtos.common.ErrorResponse;
import com.backend.Backend.dtos.usuario.UsuarioDTO;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarioService.getAllUsuarios().stream().map(usuarioMapper::mapToDto).toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(value -> ResponseEntity.ok(usuarioMapper.mapToDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @GetMapping("/info")
    public ResponseEntity<UsuarioDTO> getUsuarioByCorreo(@RequestParam() String email) {
        Optional<Usuario> usuario = usuarioService.getUsuarioByCorreo(email);
        return usuario
                .map(value -> ResponseEntity.ok(usuarioMapper.mapToDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuarioDto) {
        if (usuarioService.existeUsuarioByCorreo(usuarioDto.getCorreo())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("El correo electrónico ya está registrado"));
        }

        try {
            UsuarioDTO createdUsuario = usuarioMapper.mapToDto(usuarioService.createUsuario(usuarioDto));
            return ResponseEntity.ok(createdUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDto) {
        UsuarioDTO updatedUsuario = usuarioMapper.mapToDto(usuarioService.updateUsuario(id, usuarioDto));
        if (updatedUsuario != null) {
            return ResponseEntity.ok(updatedUsuario);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @GetMapping("/filtrar")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByEstado(@RequestParam(required = false) Boolean activo) {
        return ResponseEntity.ok(usuarioService.getUsuariosByActivo(activo).stream().map(usuarioMapper::mapToDto).toList());
    }
}
