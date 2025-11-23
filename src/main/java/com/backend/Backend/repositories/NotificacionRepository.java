package com.backend.Backend.repositories;

import com.backend.Backend.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findAllByFechaVistaNotificacionAndAUsuarioId(Date fechaVistaNotificacion, Long usuarioId);

    List<Notificacion> findAllByAUsuarioIdAndFechaNotificacionGreaterThan(Long aUsuarioId, Date fecha);

    Optional<Notificacion> findById(long id);
}
