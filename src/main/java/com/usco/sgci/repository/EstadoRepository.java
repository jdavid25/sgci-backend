package com.usco.sgci.repository;

import com.usco.sgci.entity.Estado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByNombre(String nombre);

    List<Estado> findByTipoOrderByIdAsc(String tipo);
}
