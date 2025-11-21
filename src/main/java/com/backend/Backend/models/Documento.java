package com.backend.Backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "documentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Lob
    @Column(name = "contenido", columnDefinition = "LONGBLOB")
    private byte[] contenido;

    @Column(name = "fecha_subida")
    private Date fechaSubida = new Date();

    @ManyToOne
    private Usuario usuario;
}
