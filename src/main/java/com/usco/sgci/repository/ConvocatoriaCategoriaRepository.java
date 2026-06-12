package com.usco.sgci.repository;

import com.usco.sgci.entity.ConvocatoriaCategoria;
import com.usco.sgci.entity.ConvocatoriaCategoriaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvocatoriaCategoriaRepository
        extends JpaRepository<ConvocatoriaCategoria, ConvocatoriaCategoriaId> {

    List<ConvocatoriaCategoria> findByConvocatoriaIdAndEstadoNombre(Long convocatoriaId, String estadoNombre);

    void deleteByConvocatoriaId(Long convocatoriaId);
}
