package com.backend.Backend.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidarCodigoRecuperacionDTO {
    private String email;
    private String codigo;
}
