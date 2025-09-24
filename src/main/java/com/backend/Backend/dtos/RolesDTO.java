package com.backend.Backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesDTO {

    @NotNull(message = "Name of role cannot be null")
    private String name;

    private String description;

    @JsonProperty("is_active")
    private Boolean isActive;
}
