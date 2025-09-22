package com.backend.Backend.services;

import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public UsuarioDTO createUsuario(UsuarioDTO usuarioDto) {
        Usuario usuario = mapToEntity(usuarioDto);

        if (usuario.getId() != null) {
            throw new IllegalArgumentException("El ID debe ser nulo al crear un nuevo usuario.");
        }

        usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));

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
            usuario.setImagen(usuarioDto.getUrlImagen());

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
        dto.setUrlImagen(usuario.getImagen());
        dto.setActivo(usuario.getActivo());
        return dto;
    }

    private Usuario mapToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombreApellido(dto.getNombreApellido());
        usuario.setAlias(dto.getAlias());
        usuario.setCorreo(dto.getCorreo());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        if (dto.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        return usuario;
    }
}
