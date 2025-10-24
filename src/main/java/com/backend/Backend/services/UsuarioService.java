package com.backend.Backend.services;

import com.backend.Backend.dtos.usuario.UsuarioDTO;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Rol;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.RolesRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolesRepository rolesRepository;

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
    public Usuario createUsuario(UsuarioDTO usuarioDto) {
        Usuario usuario = usuarioMapper.mapToEntity(usuarioDto);

        return usuarioRepository.save(usuario);
    }


    @Transactional
    public Usuario updateUsuario(Long id, UsuarioDTO usuarioDto) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            if (!usuario.getId().equals(id)) {
                throw new IllegalArgumentException("El ID del usuario no coincide con el ID de la base de datos.");
            }

            usuario.setNombreApellido(usuarioDto.getNombreApellido());
            usuario.setAlias(usuarioDto.getAlias());
            usuario.setCorreo(usuarioDto.getCorreo());
            usuario.setFechaNacimiento(usuarioDto.getFechaNacimiento());
            usuario.setUrlImagen(usuarioDto.getUrlImagen());
            usuario.setValidado(usuarioDto.getValidado());
            usuario.setTipoUsuario(usuarioDto.getTipoUsuario() != null ? usuarioDto.getTipoUsuario() : usuario.getTipoUsuario());

            if (usuarioDto.getRoles() != null && !usuarioDto.getRoles().isEmpty()) {
                List<Rol> roles = new ArrayList<>();
                for (Rol rol : usuarioDto.getRoles()) {
                    Optional<Rol> existingRole = rolesRepository.findById(rol.getId());
                    if (existingRole.isEmpty()) {
                        throw new IllegalArgumentException("El rol con ID " + rol.getId() + " no existe");
                    }
                    roles.add(existingRole.get());
                }
                usuario.setRoles(roles);
            }

            if (usuario.getId() == null) {
                throw new IllegalArgumentException("El ID del usuario es nulo.");
            }

            return usuarioRepository.save(usuario);
        }

        throw new IllegalArgumentException("Usuario con ID " + id + " no encontrado.");
    }


    public void deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
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
