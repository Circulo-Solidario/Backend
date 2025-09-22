package com.backend.Backend.controllers;

import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin
    @GetMapping("/info/{email}")
    public ResponseEntity<UsuarioDTO> getUsuarioByCorreo(@PathVariable("email") String email) {
        UsuarioDTO usuario = usuarioService.getUsuarioByCorreo(email);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
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
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDto) {
        UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDto);
        return ResponseEntity.ok(createdUsuario);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDto) {
        UsuarioDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDto);
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
        return ResponseEntity.ok(usuarioService.getUsuariosByActivo(activo));
    }
}
