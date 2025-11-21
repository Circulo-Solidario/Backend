package com.backend.Backend.dtos.usuario;

import com.backend.Backend.dtos.documento.DocumentoDTO;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.models.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioFiltrarDTO {
    private Long id;
    private String nombreApellido;
    private String alias;
    private String correo;
    private String urlImagen;
    private TipoUsuario tipoUsuario;
    private EstadoUsuario estado;
    private List<DocumentoDTO> documentos;
}
