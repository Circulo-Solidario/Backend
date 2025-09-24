package com.backend.Backend.services;

import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean autenticarUsuario(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        
        if (!usuarioOpt.isPresent()) {
            System.out.println("Autenticación fallida: Usuario no encontrado con correo: " + correo);
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        boolean passwordMatches = passwordEncoder.matches(contrasena, usuario.getContrasena());
        
        if (!passwordMatches) {
            System.out.println("Autenticación fallida: Contraseña incorrecta para el usuario: " + correo);
        }
        
        return passwordMatches;
    }
}
