package com.backend.Backend.services;

import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByCorreo(String email) {
        return usuarioRepository.findByCorreo(email);
    }

    @Transactional
    public Usuario createUsuario(Usuario nuevoUsuario) {
        if(nuevoUsuario.getContrasena() != null){
            nuevoUsuario.setContrasena(passwordEncoder.encode(nuevoUsuario.getContrasena()));
        }
        return usuarioRepository.save(nuevoUsuario);
    }

    @Transactional
    public Usuario updateUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        }
    }

    @Transactional
    public Usuario validarOrganizacion(Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + id + " no encontrado.");
        }
        Usuario usuario = optionalUsuario.get();
        if (usuario.getTipoUsuario() != null && usuario.getTipoUsuario().name().equals("ORGANIZACION")) {
            usuario.setValidado(true);
            return usuarioRepository.save(usuario);
        } else {
            throw new IllegalArgumentException("Solo las organizaciones pueden ser validadas con este endpoint.");
        }
    }

    @Transactional(readOnly = true)
    public Boolean existeUsuarioByCorreo(String email) {
        return usuarioRepository.findByCorreo(email).isPresent();
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuariosByActivo(Boolean activo) {
        List<Usuario> usuarios;
        if (activo != null) {
            usuarios = usuarioRepository.findByActivo(activo);
        } else {
            usuarios = usuarioRepository.findAll();
        }
        return usuarios;
    }
}
