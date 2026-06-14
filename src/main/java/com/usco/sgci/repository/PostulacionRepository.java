package com.usco.sgci.repository;

import com.usco.sgci.dto.reporte.PostulacionesConvocatoriaReporteResponse;
import com.usco.sgci.dto.reporte.ResultadoPostulacionesReporteResponse;
import com.usco.sgci.entity.Postulacion;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    List<Postulacion> findByUsuarioIdOrderByIdAsc(Long usuarioId);

    boolean existsByConvocatoriaIdAndUsuarioId(Long convocatoriaId, Long usuarioId);

    long countByConvocatoriaIdAndEstadoNombreIn(Long convocatoriaId, Collection<String> estados);

    @Query("""
            select new com.usco.sgci.dto.reporte.PostulacionesConvocatoriaReporteResponse(
                p.convocatoria.id,
                p.convocatoria.nombre,
                count(p.id)
            )
            from Postulacion p
            where p.convocatoria.estado.nombre in :estadosConvocatoria
              and p.convocatoria.deletedAt is null
            group by p.convocatoria.id, p.convocatoria.nombre
            order by p.convocatoria.nombre
            """)
    List<PostulacionesConvocatoriaReporteResponse> contarPostulacionesPorConvocatoria(
            @Param("estadosConvocatoria") Collection<String> estadosConvocatoria
    );

    @Query("""
            select new com.usco.sgci.dto.reporte.ResultadoPostulacionesReporteResponse(
                p.estado.nombre,
                count(p.id)
            )
            from Postulacion p
            where p.estado.nombre in :estadosPostulacion
              and p.convocatoria.estado.nombre in :estadosConvocatoria
              and p.convocatoria.deletedAt is null
            group by p.estado.nombre
            order by p.estado.nombre
            """)
    List<ResultadoPostulacionesReporteResponse> contarResultadosPostulaciones(
            @Param("estadosPostulacion") Collection<String> estadosPostulacion,
            @Param("estadosConvocatoria") Collection<String> estadosConvocatoria
    );
}
