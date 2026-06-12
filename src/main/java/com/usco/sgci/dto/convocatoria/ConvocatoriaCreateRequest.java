package com.usco.sgci.dto.convocatoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record ConvocatoriaCreateRequest(
        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotBlank
        String descripcion,

        @NotNull
        LocalDate fechaInicio,

        @NotNull
        LocalDate fechaFin,

        @NotNull
        @Positive
        Integer cuposDisponibles,

        @NotNull
        @Positive
        Long estadoId,

        @NotEmpty
        List<@NotNull @Positive Long> categoriaIds
) {
}
