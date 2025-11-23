package com.backend.Backend.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import com.backend.Backend.models.Rol;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.models.enums.EstadoUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    private String nombreApellido;
    private String alias;
    private String correo;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fechaNacimiento;
    private String urlImagen;
    private Boolean activo;
    private Boolean validado;
    private TipoUsuario tipoUsuario;
    private EstadoUsuario estado;
    private List<Rol> roles;
}
