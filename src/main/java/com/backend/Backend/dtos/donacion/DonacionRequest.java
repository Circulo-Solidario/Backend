package com.backend.Backend.dtos.donacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonacionRequest {
    private Long proyectoId;
    private Double monto;
}
