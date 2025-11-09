package com.backend.Backend.repositories;

import com.backend.Backend.models.Punto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntoRepository extends JpaRepository<Punto, Long> {
}
