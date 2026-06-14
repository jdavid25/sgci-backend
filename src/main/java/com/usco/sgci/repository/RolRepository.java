package com.usco.sgci.repository;

import com.usco.sgci.entity.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByIdAndDeletedAtIsNull(Long id);

    List<Rol> findByDeletedAtIsNullOrderByIdAsc();
}
