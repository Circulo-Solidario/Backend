package com.backend.Backend.repositories;

import com.backend.Backend.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalasRepository extends JpaRepository<Sala, Long> {
    @Query("SELECT r.roomName FROM Sala r WHERE (r.user1 in (:user1, :user2) AND r.user2 in (:user1, :user2)) AND r.user1 <> r.user2")
    String getRoomFromUsers(Long user1, Long user2);
}
