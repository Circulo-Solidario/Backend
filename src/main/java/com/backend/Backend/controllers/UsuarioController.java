package com.backend.Backend.controllers;

import com.backend.Backend.dtos.common.ErrorResponse;
import com.backend.Backend.dtos.usuario.EditarUsuarioDTO;
import com.backend.Backend.dtos.usuario.NuevoUsuarioDTO;
import com.backend.Backend.dtos.usuario.UsuarioDTO;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Rol;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.services.RolService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final UsuarioMapper usuarioMapper;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarioService.getAllUsuarios().stream().map(usuarioMapper::mapToDto).toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(value -> ResponseEntity.ok(usuarioMapper.mapToDto(value))).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @CrossOrigin
    @GetMapping("/info")
    public ResponseEntity<UsuarioDTO> getUsuarioByCorreo(@RequestParam() String email) {
        Optional<Usuario> usuario = usuarioService.getUsuarioByCorreo(email);
        return usuario
                .map(value -> ResponseEntity.ok(usuarioMapper.mapToDto(value)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody NuevoUsuarioDTO usuarioDto) {
        if (usuarioService.existeUsuarioByCorreo(usuarioDto.getCorreo())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("El correo electrónico ya está registrado"));
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNombreApellido(usuarioDto.getNombreApellido());
            usuario.setAlias(usuarioDto.getAlias());
            usuario.setCorreo(usuarioDto.getCorreo());
            usuario.setContrasena(usuarioDto.getContrasena());
            usuario.setFechaNacimiento(usuarioDto.getFechaNacimiento());
            usuario.setUrlImagen(usuarioDto.getUrlImagen());
            usuario.setActivo(true);
            usuario.setTipoUsuario(usuarioDto.getTipoUsuario());
            usuario.setValidado(false);

            if(usuarioDto.getRoles() != null) {
                List<Rol> roles = rolService.obtenerRolesDesdeListaId(usuarioDto.getRoles());
                if (roles.isEmpty()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("Alguno de los roles no existe"));
                }
                usuario.setRoles(roles);
            }

            UsuarioDTO createdUsuario = usuarioMapper.convertUsuarioToDTO(usuarioService.createUsuario(usuario));
            return ResponseEntity.ok(createdUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody EditarUsuarioDTO usuarioDto) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);

        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Usuario usuarioAActualizar = usuario.get();

        usuarioAActualizar.setNombreApellido(usuarioDto.getNombreApellido());
        usuarioAActualizar.setAlias(usuarioDto.getAlias());
        usuarioAActualizar.setFechaNacimiento(usuarioDto.getFechaNacimiento());
        usuarioAActualizar.setUrlImagen(usuarioDto.getUrlImagen());

        if(!usuarioDto.getRoles().isEmpty()){
            List<Rol> roles = rolService.obtenerRolesDesdeListaId(usuarioDto.getRoles());
            if (roles.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            usuarioAActualizar.setRoles(roles);
        }

        return ResponseEntity.ok(usuarioMapper.mapToDto(usuarioService.updateUsuario(usuarioAActualizar)));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        usuario.get().setActivo(false);
        usuarioService.updateUsuario(usuario.get());
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @PatchMapping("/{id}/validar")
    public ResponseEntity<UsuarioDTO> validarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(usuario.get().getValidado()){
            return ResponseEntity.badRequest().build();
        }
        usuario.get().setValidado(true);
        return ResponseEntity.ok(usuarioMapper.mapToDto(usuarioService.updateUsuario(usuario.get())));
    }

    @CrossOrigin
    @GetMapping("/filtrar")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByEstado(@RequestParam(required = false) Boolean activo) {
        return ResponseEntity.ok(usuarioService.getUsuariosByActivo(activo).stream().map(usuarioMapper::mapToDto).toList());
    }

    @CrossOrigin
    @PatchMapping("/validar/{id}")
    public ResponseEntity<?> validarOrganizacion(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.validarOrganizacion(id);
            return ResponseEntity.ok(usuarioMapper.mapToDto(usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
