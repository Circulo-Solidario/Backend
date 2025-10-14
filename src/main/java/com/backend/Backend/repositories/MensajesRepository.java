package com.backend.Backend.repositories;

import com.backend.Backend.models.Mensaje;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajesRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByRoomOrderByTimestampAsc(String room);

    @Transactional
    @Modifying
    @Query("DELETE FROM Mensaje m WHERE m.room = :room")
    void deleteByRoom(@Param("room") String room);
}
