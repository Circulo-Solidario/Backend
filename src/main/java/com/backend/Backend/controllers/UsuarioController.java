package com.backend.Backend.controllers;

import com.backend.Backend.dtos.ErrorResponse;
import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios().stream().map(usuarioMapper::mapToDto).toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioMapper.mapToDto(usuarioService.getUsuarioById(id));
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/info/{email}")
    public ResponseEntity<UsuarioDTO> getUsuarioByCorreo(@PathVariable("email") String email) {
        Usuario usuario = usuarioService.getUsuarioByCorreo(email);
        if (usuario != null) {
            return ResponseEntity.ok(usuarioMapper.mapToDto(usuario));
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/existe/{email}")
    public ResponseEntity<Boolean> existeUsuarioByCorreo(@PathVariable("email") String email) {
        return ResponseEntity.ok(usuarioService.existeUsuarioByCorreo(email));
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
