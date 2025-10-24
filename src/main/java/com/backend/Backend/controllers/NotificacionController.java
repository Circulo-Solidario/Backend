package com.backend.Backend.controllers;

import com.backend.Backend.dtos.notificacion.ReceivedNotificacionDTO;
import com.backend.Backend.dtos.notificacion.SendNotificacion;
import com.backend.Backend.mappers.NotificacionMapper;
import com.backend.Backend.models.Notificacion;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.services.NotificacionesService;
import com.backend.Backend.services.UsuarioService;
import com.pusher.rest.Pusher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin
@RequiredArgsConstructor
public class NotificacionController {
    private final Pusher pusher;
    private final NotificacionesService notificacionesService;
    private final UsuarioService usuarioService;
    private final NotificacionMapper notificacionMapper;

    @GetMapping("/{user}/notseen")
    public ResponseEntity<List<ReceivedNotificacionDTO>> getNotSeenNotification(@PathVariable Long user) {
        try {
            List<Notificacion> notifications = notificacionesService.getNotSeenNotificationFromUser(user);
            if (notifications.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<ReceivedNotificacionDTO> receivedNotifications = notifications.stream()
                    .map(notificacionMapper::mapEntityToReceiveNotificacion)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(receivedNotifications);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{user}")
    public ResponseEntity<List<ReceivedNotificacionDTO>> getNotifications(@PathVariable Long user, @RequestParam String date) {
        try {
            Date fromDate = Date.from(Instant.parse(date));
            List<Notificacion> notifications = notificacionesService.getAllNotificationsFromUserFromDate(user, fromDate);
            if (notifications.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(notifications.stream().map(notificacionMapper::mapEntityToReceiveNotificacion).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/markseen")
    public ResponseEntity<ReceivedNotificacionDTO> markSeenNotification(@RequestBody Long notificationId) {
        try {
            Optional<Notificacion> notificacion = notificacionesService.getNotificationById(notificationId);
            if(notificacion.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            Notificacion notificacionAModificar = notificacion.get();
            notificacionAModificar.setFechaVistaNotificacion(Date.from(Instant.now()));
            Notificacion notificacionActualizada = notificacionesService.saveNotificacion(notificacionAModificar);
            return ResponseEntity.ok(notificacionMapper.mapEntityToReceiveNotificacion(notificacionActualizada));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/push")
    public ResponseEntity<Notificacion> pushNotification(@RequestBody SendNotificacion notification) {
        try {
            Optional<Usuario> deUsuario = usuarioService.getUsuarioById(notification.getDeUsuario());
            Optional<Usuario> aUsuario = usuarioService.getUsuarioById(notification.getAUsuario());

            if(deUsuario.isEmpty() || aUsuario.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Notificacion notificationToSend = new Notificacion();

            notificationToSend.setDeUsuario(deUsuario.get());
            notificationToSend.setAUsuario(aUsuario.get());
            notificationToSend.setFechaNotificacion(Date.from(Instant.now()));
            notificationToSend.setMensaje(notification.getMensaje());
            notificationToSend.setFechaVistaNotificacion(null);
            notificationToSend.setTipoNotificacion(notification.getTipoNotificacion());

            Notificacion sendedNotification = notificacionesService.saveNotificacion(notificationToSend);

            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("id", sendedNotification.getId());
            notificationData.put("mensaje", sendedNotification.getMensaje());
            notificationData.put("deUsuario", sendedNotification.getDeUsuario());
            notificationData.put("fechaNotificacion", sendedNotification.getFechaNotificacion());
            notificationData.put("tipoNotificacion", sendedNotification.getTipoNotificacion());
            notificationData.put("fechaVistaNotificacion", null);

            pusher.trigger(
                    "nn-" + notificationToSend.getAUsuario().getId(),
                    "new-notification",
                    notificationData
            );
            return ResponseEntity.ok(notificationToSend);
        }catch (Exception error){
            return ResponseEntity.internalServerError().build();
        }
    }
}