package com.backend.Backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "documentos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monto;

    @Column(name = "fecha_donacion")
    private Date fechaDonacion;

    @ManyToOne
    private Usuario donador;

    @ManyToOne
    private Proyecto proyecto;
}
