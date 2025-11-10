package com.backend.Backend.repositories;

import com.backend.Backend.models.Sala;
import com.backend.Backend.models.enums.EstadoSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalasRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllBySolicitudDonadorIdAndEstado(Long solicitudDonadorId, EstadoSala estado);
    List<Sala> findAllBySolicitudSolicitanteIdAndEstado(Long solicitudSolicitanteId, EstadoSala estado);
    Optional<Sala> findBySolicitudId(Long solicitudId);
}
