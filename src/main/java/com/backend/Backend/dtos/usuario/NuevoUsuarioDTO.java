package com.backend.Backend.dtos.usuario;

import com.backend.Backend.models.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NuevoUsuarioDTO {
    private String nombreApellido;
    private String alias;
    private String correo;
    private String contrasena;
    private Date fechaNacimiento;
    private String urlImagen;
    private TipoUsuario tipoUsuario;
    private List<Long> roles;
}
