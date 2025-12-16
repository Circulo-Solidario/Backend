package com.backend.Backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Punto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private String estado;
    @ManyToOne
    @JoinColumn(name = "usuario_registro_id")
    private Usuario usuarioRegistro;
    @ManyToOne
    @JoinColumn(name = "usuario_ayudo_id")
    private Usuario usuarioAyudo;
}