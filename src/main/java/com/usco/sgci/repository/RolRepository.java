package com.usco.sgci.repository;

import com.usco.sgci.entity.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);

    Optional<Rol> findByIdAndEstadoNombre(Long id, String estadoNombre);
}
