package com.backend.Backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String correo;
    private String contrasena;
}
