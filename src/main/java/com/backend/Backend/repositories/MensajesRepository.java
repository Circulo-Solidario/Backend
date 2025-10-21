package com.backend.Backend.repositories;

import com.backend.Backend.models.Mensaje;
import com.backend.Backend.models.Sala;
import com.backend.Backend.models.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajesRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findAllBySalaId(Long id);
    List<Mensaje> findAllBySala_NombreSala(String nombreSala);
}
