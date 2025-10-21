package com.backend.Backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomDTO {
    @NotNull
    private Long usuario1;
    @NotNull
    private Long usuario2;
    @NotNull
    private Long idProducto;

    private String mensaje;
}
