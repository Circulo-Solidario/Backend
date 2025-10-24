package com.backend.Backend.dtos.usuario;

import com.backend.Backend.models.Rol;
import com.backend.Backend.models.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSimpleDTO {
    private Long id;
    private String nombreApellido;
    private String alias;
    private String correo;
    private String urlImagen;
    private TipoUsuario tipoUsuario;
}
