package com.backend.Backend.services;

import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Rol;
import com.backend.Backend.models.TipoUsuario;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.RolesRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolesRepository rolesRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioByCorreo(String email)
    {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(email);

        return optionalUsuario.orElse(null);
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
