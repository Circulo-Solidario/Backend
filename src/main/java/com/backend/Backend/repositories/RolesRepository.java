package com.backend.Backend.repositories;

import com.backend.Backend.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String name);
}