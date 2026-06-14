package com.usco.sgci.repository;

import com.usco.sgci.dto.reporte.ConvocatoriasCategoriaReporteResponse;
import com.usco.sgci.entity.ConvocatoriaCategoria;
import com.usco.sgci.entity.ConvocatoriaCategoriaId;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConvocatoriaCategoriaRepository
        extends JpaRepository<ConvocatoriaCategoria, ConvocatoriaCategoriaId> {

    List<ConvocatoriaCategoria> findByConvocatoriaId(Long convocatoriaId);

    void deleteByConvocatoriaId(Long convocatoriaId);

    @Query("""
            select new com.usco.sgci.dto.reporte.ConvocatoriasCategoriaReporteResponse(
                cc.categoria.id,
                cc.categoria.nombre,
                count(distinct cc.convocatoria.id)
            )
            from ConvocatoriaCategoria cc
            where cc.categoria.deletedAt is null
              and cc.convocatoria.deletedAt is null
              and cc.convocatoria.estado.nombre in :estadosConvocatoria
            group by cc.categoria.id, cc.categoria.nombre
            order by cc.categoria.nombre
            """)
    List<ConvocatoriasCategoriaReporteResponse> contarConvocatoriasPorCategoria(
            @Param("estadosConvocatoria") Collection<String> estadosConvocatoria
    );
}
