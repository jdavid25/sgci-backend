package com.usco.sgci.dto.postulacion;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostulacionCreateRequest(
        @NotNull
        @Positive
        Long convocatoriaId
) {
}
