package com.usco.sgci.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaCreateRequest(
        @NotBlank
        @Size(max = 100)
        String nombre
) {
}
