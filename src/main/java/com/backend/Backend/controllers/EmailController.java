package com.backend.Backend.controllers;

import com.backend.Backend.dtos.LoginRequestDTO;
import com.backend.Backend.dtos.UsuarioResponseDTO;
import com.backend.Backend.services.AuthService;
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
public class EmailController {
    private final AuthService authService;

    public EmailController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<UsuarioResponseDTO> usuarioOpt = authService.autenticarUsuario(loginRequest.getCorreo(), loginRequest.getContrasena());
        if (usuarioOpt.isPresent()) {
            UsuarioResponseDTO usuario = usuarioOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }
}
