package com.backend.Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

import com.backend.Backend.models.Roles;
import com.backend.Backend.models.TipoUsuario;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nombreApellido;
    private String alias;
    private String correo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private String urlImagen;
    private Boolean activo;
    private Boolean validado;
    private TipoUsuario tipoUsuario;
    private List<Roles> roles;
}