package com.backend.Backend.services;

import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    public List<Usuario> getUsuariosFilters(Boolean validado, TipoUsuario tipoUsuario) {
        Specification<Usuario> spec = (root, query, cb) -> null;

        if(validado != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("validado"), validado));
        }

        if(tipoUsuario != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tipoUsuario"), tipoUsuario));
        }

        return usuarioRepository.findAll(spec);
    }

    @Transactional
    public void generarCodigoRecuperacion(Long usuarioId, RecuperacionService recuperacionService) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();
        String codigo = recuperacionService.generarCodigoRecuperacion();
        usuario.setCodigoRecuperacion(codigo);
        usuario.setFechaCodigoRecuperacion(new Date());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarContrasena(Long usuarioId, String nuevaContrasena) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setCodigoRecuperacion(null);
        usuario.setFechaCodigoRecuperacion(null);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Boolean validarCodigoRecuperacion(Long usuarioId, String codigo, RecuperacionService recuperacionService) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isEmpty()) {
            return false;
        }

        Usuario usuario = optionalUsuario.get();
        
        if (usuario.getCodigoRecuperacion() == null) {
            return false;
        }

        boolean codigoCorrecto = usuario.getCodigoRecuperacion().equals(codigo);
        boolean codigoNoExpirado = recuperacionService.validarCodigoRecuperacion(
            usuario.getCodigoRecuperacion(),
            usuario.getFechaCodigoRecuperacion()
        );

        return codigoCorrecto && codigoNoExpirado;
    }
}
