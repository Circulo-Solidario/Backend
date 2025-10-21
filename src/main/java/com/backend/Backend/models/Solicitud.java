package com.backend.Backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Usuario aUsuario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Boolean aceptada = false;
}
