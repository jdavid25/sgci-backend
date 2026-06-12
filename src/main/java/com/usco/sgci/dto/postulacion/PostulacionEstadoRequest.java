package com.usco.sgci.dto.postulacion;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostulacionEstadoRequest(
        @NotNull
        @Positive
        Long estadoId
) {
}
