package com.backend.Backend.services;

import com.backend.Backend.models.Notificacion;
import com.backend.Backend.repositories.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificacionesService {
    private final NotificacionRepository notificationsRepository;

    public Notificacion saveNotificacion(Notificacion notification) {
        return notificationsRepository.save(notification);
    }

    public List<Notificacion> getNotSeenNotificationFromUser(Long userId) {
        return notificationsRepository.findAllByFechaVistaNotificacionAndAUsuarioId(null, userId);
    }

    public List<Notificacion> getAllNotificationsFromUserFromDate(Long userId, Date date) {
        return notificationsRepository.findAllByAUsuarioIdAndFechaNotificacionGreaterThan(userId, date);
    }

    public Optional<Notificacion> getNotificationById(Long id) {
        return notificationsRepository.findById(id);
    }

}
