package com.backend.Backend.repositories;

import com.backend.Backend.models.Notificacion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findNotificationsBySeenDateAndToUser(Date seenDate, Long toUser);

    List<Notificacion> findNotificationsByToUserAndDateGreaterThan(Long toUser, Instant date);

    @Transactional
    @Modifying
    @Query("UPDATE Notificacion SET seenDate = :date WHERE id = :id")
    void updateSeenDate(@Param("id") Long id, @Param("date") Date date);
}
