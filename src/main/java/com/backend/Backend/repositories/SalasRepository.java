package com.backend.Backend.repositories;

import com.backend.Backend.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalasRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllBySolicitudDonadorId(Long solicitudDonadorId);
    List<Sala> findAllBySolicitudSolicitanteId(Long solicitudSolicitanteId);
    Optional<Sala> findBySolicitudId(Long solicitudId);
}
