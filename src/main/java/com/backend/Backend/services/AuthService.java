package com.backend.Backend.services;

import com.backend.Backend.models.Usuario;
import com.backend.Backend.dtos.UsuarioResponseDTO;
import com.backend.Backend.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public Optional<UsuarioResponseDTO> autenticarUsuario(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(contrasena, usuarioOpt.get().getContrasena())) {
            UsuarioResponseDTO usuarioDTO = modelMapper.map(usuarioOpt.get(), UsuarioResponseDTO.class);
            return Optional.of(usuarioDTO);
        }
        return Optional.empty();
    }
}
