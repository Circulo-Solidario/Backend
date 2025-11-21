package com.backend.Backend.dtos.documento;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoDTO {
    private Long id;
    private String nombre;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaSubida;
}
