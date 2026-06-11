package com.usco.sgci.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ConvocatoriaCategoriaId implements Serializable {

    @Column(name = "convocatoria_id", nullable = false)
    private Long convocatoriaId;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;
}
