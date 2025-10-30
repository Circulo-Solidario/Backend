package com.backend.Backend.repositories;

import com.backend.Backend.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajesRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findAllBySalaIdOrderByFechaMensajeAsc(Long salaId);
}
