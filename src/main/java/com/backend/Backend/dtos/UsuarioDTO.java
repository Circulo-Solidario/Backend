package com.backend.Backend.dtos;

import com.backend.Backend.config.FechaNacimientoValida;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombreApellido;
    private String alias;
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser valido")
    @Column(unique = true)
    private String correo;
    @NotBlank
    @Size(min = 5, message = "La contraseña debe ser de al menos 5 caracteres")
    private String contrasena;
    @FechaNacimientoValida
    private Date fechaNacimiento;
    private String urlImagen;
    private Boolean activo;
}
