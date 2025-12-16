package com.backend.Backend.dtos.estadistica;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonacionDetalle {
    private String organizacion;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fecha;
    private double monto;
}
