package com.backend.Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

import com.backend.Backend.models.Rol;
import com.backend.Backend.models.TipoUsuario;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
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
    private List<Rol> roles;
}