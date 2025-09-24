package com.backend.Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolesDTO {
    private String nombreApellido;
    private String alias;
    private String correo;
    private String contrasena;
    private Date fechaNacimiento;
    private String urlImagen;
    private Boolean activo;
    private List<String> roles;
}


