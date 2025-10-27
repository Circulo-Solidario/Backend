package com.backend.Backend.mappers;

import com.backend.Backend.dtos.UsuarioDTO;
import com.backend.Backend.dtos.UsuarioResponseDTO;
import com.backend.Backend.models.Rol;
import com.backend.Backend.models.TipoUsuario;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public UsuarioDTO mapToDto(Usuario usuario) {
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

    public UsuarioResponseDTO mapToResponseDto(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombreApellido(usuario.getNombreApellido())
                .alias(usuario.getAlias())
                .correo(usuario.getCorreo())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .urlImagen(usuario.getUrlImagen())
                .activo(usuario.getActivo())
                .tipoUsuario(usuario.getTipoUsuario())
                .roles(usuario.getRoles())
                .build();
    }

    public Usuario mapToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombreApellido(dto.getNombreApellido());
        usuario.setAlias(dto.getAlias());
        usuario.setCorreo(dto.getCorreo());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        usuario.setUrlImagen(dto.getUrlImagen());
        usuario.setTipoUsuario(dto.getTipoUsuario() != null ? dto.getTipoUsuario() : TipoUsuario.USUARIO);
        if (dto.getValidado() != null) {
            usuario.setValidado(dto.getValidado());
        } else {
            usuario.setValidado(usuario.getTipoUsuario() == TipoUsuario.ORGANIZACION ? false : true);
        }

        if (dto.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        if (usuario.getTipoUsuario() == TipoUsuario.USUARIO) {
            List<Rol> roles = new ArrayList<>();
            if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
                for (Rol rol : dto.getRoles()) {
                    Optional<Rol> existingRole = rolesRepository.findById(rol.getId());
                    if (!existingRole.isPresent()) {
                        throw new IllegalArgumentException("El rol con ID " + rol.getId() + " no existe");
                    }
                    roles.add(existingRole.get());
                }
            } else {
                Optional<Rol> defaultRole = rolesRepository.findByNombre("USER");
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

    public UsuarioDTO convertUsuarioToDTO(Usuario usuario) {
        if (usuario == null) return null;
        com.backend.Backend.dtos.UsuarioDTO dto = new com.backend.Backend.dtos.UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreApellido(usuario.getNombreApellido());
        dto.setAlias(usuario.getAlias());
        dto.setCorreo(usuario.getCorreo());
        dto.setContrasena(usuario.getContrasena());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setUrlImagen(usuario.getUrlImagen());
        dto.setActivo(usuario.getActivo());
        dto.setValidado(usuario.getValidado());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setRoles(usuario.getRoles());
        return dto;
    }
}
