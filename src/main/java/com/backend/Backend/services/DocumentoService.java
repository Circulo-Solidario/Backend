package com.backend.Backend.services;

import com.backend.Backend.dtos.documento.DocumentoRequest;
import com.backend.Backend.models.Documento;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.EstadoUsuario;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.repositories.DocumentoRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Documento createDocumento(Long usuarioId, String nombre, byte[] contenido) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        if (usuario.getTipoUsuario() != TipoUsuario.ORGANIZACION) {
            throw new IllegalArgumentException("Solo usuarios de tipo ORGANIZACION pueden subir documentos");
        }
        Documento documento = new Documento();
        documento.setNombre(nombre);
        documento.setContenido(contenido);
        documento.setUsuario(usuario);
        Documento documentoGuardado = documentoRepository.save(documento);
        
        // Actualizar estado del usuario a PENDIENTE después de subir documento
        usuario.setEstado(EstadoUsuario.PENDIENTE);
        usuarioRepository.save(usuario);
        
        return documentoGuardado;
    }

    @Transactional(readOnly = true)
    public List<Documento> getDocumentosByUsuario(Long usuarioId) {
        return documentoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Documento getDocumento(Long usuarioId, Long documentoId) {
        Optional<Documento> documentoOpt = documentoRepository.findById(documentoId);
        if (documentoOpt.isEmpty()) {
            throw new IllegalArgumentException("Documento no encontrado");
        }
        Documento documento = documentoOpt.get();
        if (documento.getUsuario() == null || !documento.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("El documento no pertenece al usuario indicado");
        }
        return documento;
    }

    @Transactional
    public void deleteDocumento(Long usuarioId, Long documentoId) {
        Optional<Documento> documentoOpt = documentoRepository.findById(documentoId);
        if (documentoOpt.isEmpty()) {
            throw new IllegalArgumentException("Documento no encontrado");
        }
        Documento documento = documentoOpt.get();
        if (documento.getUsuario() == null || !documento.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("El documento no pertenece al usuario indicado");
        }
        documentoRepository.delete(documento);
        
        // Verificar si el usuario tiene más documentos, si no, cambiar estado a SIN_DOCUMENTO
        List<Documento> documentosRestantes = documentoRepository.findByUsuarioId(usuarioId);
        if (documentosRestantes.isEmpty()) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuario.setEstado(EstadoUsuario.SIN_DOCUMENTO);
                usuarioRepository.save(usuario);
            }
        }
    }
}
