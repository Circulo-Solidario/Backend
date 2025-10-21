package com.backend.Backend.controllers;

import com.backend.Backend.dtos.LoginRequestDTO;
import com.backend.Backend.dtos.UsuarioResponseDTO;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class EmailController {
    private final AuthService authService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<Usuario> usuario = authService.autenticarUsuario(loginRequest.getCorreo(), loginRequest.getContrasena());
        if (usuario.isPresent()) {
            UsuarioResponseDTO usuarioResponse = usuarioMapper.mapToResponseDto(usuario.get());
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("usuario", usuarioResponse);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }
}
