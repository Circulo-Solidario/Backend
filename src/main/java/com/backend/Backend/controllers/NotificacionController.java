package com.backend.Backend.controllers;

import com.backend.Backend.dtos.ReceivedNotificacion;
import com.backend.Backend.dtos.SendNotificacion;
import com.backend.Backend.models.Notificacion;
import com.backend.Backend.services.NotificacionesService;
import com.pusher.rest.Pusher;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin
public class NotificacionController {
    private final Pusher pusher;
    private final ModelMapper modelMapper;
    private final NotificacionesService notificacionesService;

    public NotificacionController(Pusher pusher, NotificacionesService notificacionesService, ModelMapper modelMapper) {
        this.pusher = pusher;
        this.notificacionesService = notificacionesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{user}/notseen")
    public ResponseEntity<List<ReceivedNotificacion>> getNotSeenNotification(@PathVariable Long user) {
        try {
            List<Notificacion> notifications = notificacionesService.getNotSeenNotificationFromUser(user);
            if (notifications.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<ReceivedNotificacion> receivedNotifications = notifications.stream()
                    .map(notification -> modelMapper.map(notification, ReceivedNotificacion.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(receivedNotifications);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{user}")
    public ResponseEntity<List<ReceivedNotificacion>> getNotifications(@PathVariable Long user, @RequestParam(required = false) String date) {
        try {
            Instant fromDate = Instant.parse(date);
            List<Notificacion> notifications = notificacionesService.getAllNotificationsFromUserFromDate(user, fromDate);
            if (notifications.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<ReceivedNotificacion> receivedNotifications = notifications.stream()
                    .map(notification -> modelMapper.map(notification, ReceivedNotificacion.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(receivedNotifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/markseen")
    public ResponseEntity<Boolean> markSeenNotification(@RequestBody Long notificationId) {
        try {
            notificacionesService.markSeenNotification(notificationId, Date.from(Instant.now()));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/push")
    public ResponseEntity<Notificacion> pushNotification(@RequestBody SendNotificacion notification) {
        try {
            Notificacion notificationToSend = new Notificacion();

            notificationToSend.setFromUser(notification.getFromUser());
            notificationToSend.setToUser(notification.getToUser());
            notificationToSend.setDate(Instant.now());
            notificationToSend.setMessage(notification.getMessage());
            notificationToSend.setSeenDate(null);

            Notificacion sendedNotification = notificacionesService.pushNotification(notificationToSend);

            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("id", sendedNotification.getId());
            notificationData.put("message", sendedNotification.getMessage());
            notificationData.put("fromUser", sendedNotification.getFromUser());
            notificationData.put("date", sendedNotification.getDate());
            notificationData.put("seenDate", null);

            pusher.trigger(
                    "nm-" + notificationToSend.getToUser(),
                    "new-notification",
                    notificationData
            );

            log.info("Notification sent successfully");
            return ResponseEntity.ok(notificationToSend);
        }catch (Exception error){
            log.error("Failed to push notification: {}", notification, error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}