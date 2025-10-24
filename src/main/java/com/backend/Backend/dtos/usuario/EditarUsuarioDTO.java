package com.backend.Backend.dtos.usuario;

import com.backend.Backend.models.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class EditarUsuarioDTO {
    private String nombreApellido;
    private String alias;
    private Date fechaNacimiento;
    private String urlImagen;
    private List<Long> roles;
}
