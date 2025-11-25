package com.backend.Backend.models;

import com.backend.Backend.models.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import com.backend.Backend.models.enums.EstadoUsuario;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_apellido", nullable = false)
    private String nombreApellido;

    private String alias;
    @Column(unique = true)
    private String correo;
    private String contrasena;

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    private String urlImagen;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "mercadopago_access_token")
    private String mercadoPagoAccessToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;

    @Column(name = "validado", nullable = false)
    private Boolean validado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoUsuario estado;

    @Column(name = "codigo_recuperacion")
    private String codigoRecuperacion;

    @Column(name = "fecha_codigo_recuperacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCodigoRecuperacion;
}
