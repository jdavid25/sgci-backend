package com.usco.sgci.repository;

import com.usco.sgci.entity.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByIdAndEstadoNombre(Long id, String estadoNombre);

    List<Categoria> findByEstadoNombreOrderByIdAsc(String estadoNombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);
}
