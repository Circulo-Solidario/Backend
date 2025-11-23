package com.backend.Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String mensaje;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; //Usuario que postea el mensaje

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Sala sala;//Sala a la que corresponde el mensaje

    @Column(name = "fecha_mensaje", nullable = false)
    private Date fechaMensaje;

    @Column(name = "fecha_visto_mensaje")
    private Date fechaVistoMensaje;
}
