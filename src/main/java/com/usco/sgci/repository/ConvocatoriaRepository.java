package com.usco.sgci.repository;

import com.usco.sgci.entity.Convocatoria;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {

    Optional<Convocatoria> findByIdAndEstadoNombreIn(Long id, Collection<String> estados);

    List<Convocatoria> findByEstadoNombreInOrderByIdAsc(Collection<String> estados);
}
