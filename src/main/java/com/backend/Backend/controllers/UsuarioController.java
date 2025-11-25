package com.backend.Backend.controllers;

import com.backend.Backend.dtos.common.ErrorResponse;
import com.backend.Backend.dtos.usuario.EditarUsuarioDTO;
import com.backend.Backend.dtos.usuario.NuevoUsuarioDTO;
import com.backend.Backend.dtos.usuario.UsuarioDTO;
import com.backend.Backend.dtos.usuario.UsuarioSimpleDTO;
import com.backend.Backend.dtos.usuario.UsuarioFiltrarDTO;
import com.backend.Backend.dtos.usuario.CambiarContrasenaDTO;
import com.backend.Backend.dtos.usuario.ValidarCodigoRecuperacionDTO;
import com.backend.Backend.dtos.usuario.ResultadoRecuperacionDTO;
import com.backend.Backend.dtos.documento.DocumentoDTO;
import com.backend.Backend.services.DocumentoService;
import com.backend.Backend.services.EmailService;
import com.backend.Backend.services.RecuperacionService;
import com.backend.Backend.mappers.UsuarioMapper;
import com.backend.Backend.models.Rol;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.models.enums.EstadoUsuario;
import com.backend.Backend.dtos.usuario.ActualizarEstadoDTO;
import com.backend.Backend.services.RolService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final UsuarioMapper usuarioMapper;
    private final DocumentoService documentoService;
    private final EmailService emailService;
    private final RecuperacionService recuperacionService;

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
            
            // Establecer estado por defecto como SIN_DOCUMENTO
            usuario.setEstado(EstadoUsuario.SIN_DOCUMENTO);

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
        if (usuarioDto.getMercadoPagoAccessToken() != null) {
            if (usuarioAActualizar.getTipoUsuario() == TipoUsuario.ORGANIZACION) {
                usuarioAActualizar.setMercadoPagoAccessToken(usuarioDto.getMercadoPagoAccessToken());
            } else {
                // Opcional: se ignora o rechaza si no es organización.
            }
        }

        if(usuarioDto.getRoles() != null && !usuarioDto.getRoles().isEmpty()){
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
    public ResponseEntity<?> validarUsuario(@PathVariable Long id, @RequestBody ActualizarEstadoDTO estadoDto) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        
        // Validar que sea una organización
        if(usuario.get().getTipoUsuario() != TipoUsuario.ORGANIZACION){
            return ResponseEntity.badRequest().body(new ErrorResponse("Solo se pueden validar usuarios de tipo ORGANIZACION"));
        }

        usuario.get().setEstado(estadoDto.getEstado());

        if(estadoDto.getEstado() == EstadoUsuario.VALIDADO){
            usuario.get().setValidado(true);
        }

        return ResponseEntity.ok(usuarioMapper.mapToDto(usuarioService.updateUsuario(usuario.get())));
    }

    @CrossOrigin
    @GetMapping("/filtrar")
    public ResponseEntity<List<UsuarioFiltrarDTO>> getUsuariosFilters(@RequestParam(required = false) Boolean validado, @RequestParam(required = false)TipoUsuario tipoUsuario) {
        List<UsuarioFiltrarDTO> result = usuarioService.getUsuariosFilters(validado, tipoUsuario).stream().map(usuario -> {
            UsuarioFiltrarDTO.UsuarioFiltrarDTOBuilder builder = UsuarioFiltrarDTO.builder()
                    .id(usuario.getId())
                    .nombreApellido(usuario.getNombreApellido())
                    .alias(usuario.getAlias())
                    .correo(usuario.getCorreo())
                    .urlImagen(usuario.getUrlImagen())
                    .tipoUsuario(usuario.getTipoUsuario())
                    .estado(usuario.getEstado());

            // obtener documentos vinculados
            var documentos = documentoService.getDocumentosByUsuario(usuario.getId());
            if (documentos != null && !documentos.isEmpty()) {
                List<DocumentoDTO> docs = documentos.stream().map(d -> DocumentoDTO.builder()
                        .id(d.getId())
                        .nombre(d.getNombre())
                        .fechaSubida(d.getFechaSubida())
                        .build()).toList();
                builder.documentos(docs);
            }

            return builder.build();
        }).toList();

        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/{id}/documentos")
    public ResponseEntity<?> getDocumentosByUsuario(@PathVariable Long id) {
        try {
            var documentos = documentoService.getDocumentosByUsuario(id);
            List<DocumentoDTO> docs = documentos.stream().map(d -> DocumentoDTO.builder()
                    .id(d.getId())
                    .nombre(d.getNombre())
                    .fechaSubida(d.getFechaSubida())
                    .build()).toList();
            return ResponseEntity.ok(docs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/documentos")
    public ResponseEntity<?> uploadDocumento(@PathVariable Long id, @RequestParam MultipartFile archivo) {
        try {
            var saved = documentoService.createDocumento(id, archivo.getOriginalFilename(), archivo.getBytes());
            DocumentoDTO dto = DocumentoDTO.builder()
                    .id(saved.getId())
                    .nombre(saved.getNombre())
                    .fechaSubida(saved.getFechaSubida())
                    .build();
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error al procesar el archivo: " + e.getMessage()));
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}/documentos/{docId}")
    public ResponseEntity<Void> deleteDocumento(@PathVariable Long id, @PathVariable Long docId) {
        try {
            documentoService.deleteDocumento(id, docId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @CrossOrigin
    @GetMapping("/{id}/documentos/{docId}/descargar")
    public ResponseEntity<?> descargarDocumento(@PathVariable Long id, @PathVariable Long docId) {
        try {
            var documento = documentoService.getDocumento(id, docId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documento.getNombre() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(documento.getContenido());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @CrossOrigin
    @GetMapping("/recuperar-contrasena")
    public ResponseEntity<?> recuperarContrasena(@RequestParam String email) {
        try {
            Optional<Usuario> usuario = usuarioService.getUsuarioByCorreo(email);
            if (usuario.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El correo no está registrado"));
            }

            usuarioService.generarCodigoRecuperacion(usuario.get().getId(), recuperacionService);
            String codigo = usuario.get().getCodigoRecuperacion();
            emailService.enviarCodigoRecuperacion(email, codigo);

            return ResponseEntity.ok(new ErrorResponse("Código de recuperación enviado al correo"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    @CrossOrigin
    @PostMapping("/validar-codigo")
    public ResponseEntity<?> validarCodigoRecuperacion(@RequestBody ValidarCodigoRecuperacionDTO dto) {
        try {
            Optional<Usuario> usuario = usuarioService.getUsuarioByCorreo(dto.getEmail());
            if (usuario.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El correo no está registrado"));
            }

            Boolean valido = usuarioService.validarCodigoRecuperacion(usuario.get().getId(), dto.getCodigo(), recuperacionService);
            
            if (!valido) {
                return ResponseEntity.ok(ResultadoRecuperacionDTO.builder()
                        .valido(false)
                        .mensaje("Código inválido o expirado")
                        .build());
            }

            return ResponseEntity.ok(ResultadoRecuperacionDTO.builder()
                    .valido(true)
                    .mensaje("Código válido")
                    .usuarioId(usuario.get().getId())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    @CrossOrigin
    @PatchMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(@PathVariable Long id, @RequestBody CambiarContrasenaDTO dto) {
        try {
            Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
            if (usuario.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            usuarioService.cambiarContrasena(id, dto.getNuevaContrasena());
            
            return ResponseEntity.ok(new ErrorResponse("Contraseña cambiada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}
