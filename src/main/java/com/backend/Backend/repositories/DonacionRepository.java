package com.backend.Backend.repositories;

import com.backend.Backend.models.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {
}
