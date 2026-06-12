package com.usco.sgci.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String nombre
) {
}
