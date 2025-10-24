package com.backend.Backend.models;

import com.backend.Backend.models.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "de_usuario")
    private Usuario deUsuario;

    @ManyToOne
    @JoinColumn(name = "a_usuario")
    private Usuario ausuario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_solicitud")
    private Date fechaSolicitud;

    private String mensaje;
}
