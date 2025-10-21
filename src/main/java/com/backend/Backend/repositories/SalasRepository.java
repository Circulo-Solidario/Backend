package com.backend.Backend.repositories;

import com.backend.Backend.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalasRepository extends JpaRepository<Sala, Long> {
    @Query("SELECT r.nombreSala FROM Sala r WHERE (r.usuario1 in (:user1, :user2) AND r.usuario2 in (:user1, :user2)) AND r.usuario1 <> r.usuario2 AND r.producto = :productId")
    String getRoomFromUsers(Long user1, Long user2, Long productId);

    Optional<Sala> findByNombreSala(String nombreSala);
}
