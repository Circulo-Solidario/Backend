package com.backend.Backend.services;

import com.backend.Backend.models.Notificacion;
import com.backend.Backend.repositories.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class NotificacionesService {
    private final NotificacionRepository notificationsRepository;

    public NotificacionesService(NotificacionRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    public Notificacion pushNotification(Notificacion notification) {
        return notificationsRepository.save(notification);
    }

    public List<Notificacion> getNotSeenNotificationFromUser(Long user) {
        return notificationsRepository.findNotificationsBySeenDateAndToUser(null, user);
    }

    public List<Notificacion> getAllNotificationsFromUserFromDate(Long id, Instant date) {
        return notificationsRepository.findNotificationsByToUserAndDateGreaterThan(id, date);
    }

    public void markSeenNotification(Long id, Date date) {
        notificationsRepository.updateSeenDate(id, date);
    }

}
