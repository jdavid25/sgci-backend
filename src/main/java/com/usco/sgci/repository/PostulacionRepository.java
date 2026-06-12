package com.usco.sgci.repository;

import com.usco.sgci.entity.Postulacion;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    List<Postulacion> findByUsuarioIdOrderByIdAsc(Long usuarioId);

    boolean existsByConvocatoriaIdAndUsuarioId(Long convocatoriaId, Long usuarioId);

    long countByConvocatoriaIdAndEstadoNombreIn(Long convocatoriaId, Collection<String> estados);
}
