package com.backend.Backend.mappers;

import com.backend.Backend.dtos.usuario.UsuarioDTO;
import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.models.Rol;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.models.enums.EstadoUsuario;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    private final RolesRepository rolesRepository;

    public UsuarioDTO mapToDto(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombreApellido(usuario.getNombreApellido())
                .alias(usuario.getAlias())
                .correo(usuario.getCorreo())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .urlImagen(usuario.getUrlImagen())
                .activo(usuario.getActivo())
                .validado(usuario.getValidado())
                .tipoUsuario(usuario.getTipoUsuario())
                .estado(usuario.getEstado())
                .roles(usuario.getRoles())
                .mercadoPagoAccessToken(usuario.getMercadoPagoAccessToken())
                // NO incluir codigoRecuperacion
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

        if (dto instanceof UsuarioDTO) {
            UsuarioDTO usuarioDTO = (UsuarioDTO) dto;
            usuario.setMercadoPagoAccessToken(usuarioDTO.getMercadoPagoAccessToken());
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
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreApellido(usuario.getNombreApellido());
        dto.setAlias(usuario.getAlias());
        dto.setCorreo(usuario.getCorreo());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setUrlImagen(usuario.getUrlImagen());
        dto.setActivo(usuario.getActivo());
        dto.setValidado(usuario.getValidado());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setEstado(usuario.getEstado());
        dto.setRoles(usuario.getRoles());
        dto.setMercadoPagoAccessToken(usuario.getMercadoPagoAccessToken());
        // NO incluir codigoRecuperacion
        return dto;
    }

    public UsuarioSimpleDTO mapEntityToUsuarioSimple(Usuario usuario){
        return UsuarioSimpleDTO.builder()
                .id(usuario.getId())
                .nombreApellido(usuario.getNombreApellido())
                .alias(usuario.getAlias())
                .correo(usuario.getCorreo())
                .urlImagen(usuario.getUrlImagen())
                .tipoUsuario(usuario.getTipoUsuario())
                .build();
    }
}
