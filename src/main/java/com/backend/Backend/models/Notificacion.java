package com.backend.Backend.models;

import com.backend.Backend.models.enums.TipoNotificaciones;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mensaje;//Mensaje de la notificacion

    @ManyToOne
    @JoinColumn(name = "a_usuario", nullable = false)
    private Usuario AUsuario;

    @ManyToOne
    @JoinColumn(name = "de_usuario", nullable = false)
    private Usuario deUsuario;

    @Column(name = "fecha_notificacion", nullable = false)
    private Date fechaNotificacion;

    @Column(name = "fecha_vista")
    private Date fechaVistaNotificacion = null;

    @Column(name = "tipo_notificacion")
    private TipoNotificaciones tipoNotificacion;
}
