package com.backend.Backend.repositories;

import com.backend.Backend.models.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    List<Donacion> findAllByDonadorId(Long donadorId);
}
