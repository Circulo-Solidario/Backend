package com.backend.Backend.services;

import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.models.Roles;
import com.backend.Backend.models.TipoUsuario;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.RolesRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesRepository rolesRepository;

    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public UsuarioDTO getUsuarioById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(this::mapToDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioByCorreo(String email)
    {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(email);

        if(optionalUsuario.isEmpty())
        {
            return null;
        };

        Usuario usuario = optionalUsuario.get();

        return mapToDto(usuario);
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDto) {
        Usuario usuario = mapToEntity(usuarioDto);

        if (usuario.getId() != null) {
            throw new IllegalArgumentException("El ID debe ser nulo al crear un nuevo usuario.");
        }
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        if (savedUsuario.getId() == null) {
            throw new IllegalStateException("El ID del Usuario no fue generado correctamente.");
        }

        return mapToDto(savedUsuario);
    }


    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDto) {
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
            usuario.setRoles(usuarioDto.getRoles());
            usuario.setUrlImagen(usuarioDto.getUrlImagen());
            usuario.setValidado(usuario.getValidado());

            if (usuario.getId() == null) {
                throw new IllegalArgumentException("El ID del usuario es nulo.");
            }

            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return mapToDto(updatedUsuario);
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
    public List<UsuarioDTO> getUsuariosByActivo(Boolean activo) {
        List<Usuario> usuarios;
        if (activo != null) {
            usuarios = usuarioRepository.findByActivo(activo);
        } else {
            usuarios = usuarioRepository.findAll();
        }
        return usuarios.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private UsuarioDTO mapToDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreApellido(usuario.getNombreApellido());
        dto.setAlias(usuario.getAlias());
        dto.setCorreo(usuario.getCorreo());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setUrlImagen(usuario.getUrlImagen());
        dto.setActivo(usuario.getActivo());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setRoles(usuario.getRoles());
        dto.setValidado(usuario.getValidado());
        return dto;
    }

    private Usuario mapToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombreApellido(dto.getNombreApellido());
        usuario.setAlias(dto.getAlias());
        usuario.setCorreo(dto.getCorreo());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        usuario.setUrlImagen(dto.getUrlImagen());
        usuario.setTipoUsuario(dto.getTipoUsuario() != null ? dto.getTipoUsuario() : TipoUsuario.USUARIO);
        usuario.setValidado(dto.getValidado());

        if (dto.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        if (usuario.getTipoUsuario() == TipoUsuario.USUARIO) {
            List<Roles> roles = new ArrayList<>();
            if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
                for (Roles rol : dto.getRoles()) {
                    Optional<Roles> existingRole = rolesRepository.findById(rol.getId());
                    if (!existingRole.isPresent()) {
                        throw new IllegalArgumentException("El rol con ID " + rol.getId() + " no existe");
                    }
                    roles.add(existingRole.get());
                }
            } else {
                Optional<Roles> defaultRole = rolesRepository.findByName("USER");
                if (!defaultRole.isPresent()) {
                    throw new IllegalStateException("No se encontró el rol por defecto 'USER'");
                }
                roles.add(defaultRole.get());
            }
            usuario.setRoles(roles);
        } else {
            usuario.setRoles(new ArrayList<>());
        }

        return usuario;
    }
}
