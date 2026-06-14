package com.usco.sgci.repository;

import com.usco.sgci.entity.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByIdAndDeletedAtIsNull(Long id);

    List<Categoria> findByDeletedAtIsNullOrderByIdAsc();

    boolean existsByNombreAndDeletedAtIsNull(String nombre);

    boolean existsByNombreAndIdNotAndDeletedAtIsNull(String nombre, Long id);
}
