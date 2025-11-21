package com.backend.Backend.dtos.usuario;

import com.backend.Backend.models.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarEstadoDTO {
    private EstadoUsuario estado;
}
