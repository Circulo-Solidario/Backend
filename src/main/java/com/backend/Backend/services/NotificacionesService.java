package com.backend.Backend.services;

import com.backend.Backend.models.Notificacion;
import com.backend.Backend.repositories.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionesService {
    private final NotificacionRepository notificationsRepository;

    public Notificacion pushNotification(Notificacion notification) {
        return notificationsRepository.save(notification);
    }

    public List<Notificacion> getNotSeenNotificationFromUser(Long userId) {
        return notificationsRepository.findAllByFechaVistaNotificacionAndAUsuarioId(null, userId);
    }

    public List<Notificacion> getAllNotificationsFromUserFromDate(Long userId, Date date) {
        return notificationsRepository.findAllByAUsuarioIdAndFechaNotificacionGreaterThan(userId, date);
    }

    public void markSeenNotification(Long id, Date date) {
        notificationsRepository.updateSeenDate(id, date);
    }

}
