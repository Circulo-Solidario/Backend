package com.backend.Backend.controllers;

import com.backend.Backend.dtos.estadistica.EstadisticaResponse;
import com.backend.Backend.dtos.estadistica.PersonalEstadisticaResponse;
import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.Punto;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.repositories.PuntoRepository;
import com.backend.Backend.services.ProductoService;
import com.backend.Backend.services.ProyectoService;
import com.backend.Backend.services.UsuarioService;
import com.backend.Backend.services.NotificacionesService;
import com.backend.Backend.services.MensajesService;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estadisticas/global")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstadisticaController {

    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final ProyectoService proyectoService;
    private final PuntoRepository puntoRepository;
    private final NotificacionesService notificacionesService;
    private final MensajesService mensajesService;

    @GetMapping
    public ResponseEntity<EstadisticaResponse> getEstadisticas() {

        EstadisticaResponse stats = new EstadisticaResponse();

        // Usuarios
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        stats.setTotalUsuarios(usuarios.size());
        stats.setUsuariosActivos(usuarios.stream().filter(Usuario::getActivo).count());
        stats.setUsuariosInactivos(usuarios.stream().filter(u -> !u.getActivo()).count());

        stats.setTotalOrganizaciones(usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.ORGANIZACION).count());
        stats.setTotalUsuarios(usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.USUARIO).count());

        // Productos
        List<Producto> productos = productoService.getAllProductos();

        stats.setTotalProductos(productos.size());
        stats.setProductosDisponibles(productos.stream().filter(p -> p.getEstado() == EstadoProducto.DISPONIBLE).count());
        stats.setProductosSolicitados(productos.stream().filter(p -> p.getEstado() == EstadoProducto.SOLICITADO).count());
        stats.setProductosReservados(productos.stream().filter(p -> p.getEstado() == EstadoProducto.RESERVADO).count());
        stats.setProductosEntregados(productos.stream().filter(p -> p.getEstado() == EstadoProducto.ENTREGADO).count());

        // Proyectos
        List<Proyecto> proyectos = proyectoService.getAllProyectos();

        stats.setTotalProyectos(proyectos.size());
        stats.setProyectosActivos(proyectos.stream().filter(p -> p.getEstado() == EstadoProyecto.ACTIVO).count());
        stats.setProyectosCancelados(proyectos.stream().filter(p -> p.getEstado() == EstadoProyecto.CANCELADO).count());
        stats.setProyectosFinalizadosExitosos(proyectos.stream().filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_EXITOSO).count());
        stats.setProyectosFinalizadosNoExitosos(proyectos.stream().filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_NO_EXITOSO).count());

        stats.setTotalRecaudado(
                proyectos.stream().mapToDouble(Proyecto::getRecaudado).sum()
        );

        // Puntos
        List<Punto> puntos = puntoRepository.findAll();

        stats.setTotalPuntos(puntos.size());
        stats.setPuntosPendientes(puntos.stream().filter(p -> p.getEstado().equals("pendiente")).count());
        stats.setPuntosAtendidos(puntos.stream().filter(p -> p.getEstado().equals("atendido")).count());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/personal/{id}")
    public ResponseEntity<PersonalEstadisticaResponse> getEstadisticasPersonales(@PathVariable Long id) {

        PersonalEstadisticaResponse resp = new PersonalEstadisticaResponse();

        List<Proyecto> proyectosUsuario = proyectoService.getProyectos(id, null, null);
        resp.setProyectosCreados(proyectosUsuario.size());
        resp.setProyectosActivos(proyectosUsuario.stream().filter(p -> p.getEstado() == EstadoProyecto.ACTIVO).count());
        resp.setProyectosFinalizados(proyectosUsuario.stream().filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_EXITOSO || p.getEstado() == EstadoProyecto.FINALIZADO_NO_EXITOSO).count());
        resp.setTotalRecaudadoPorProyectos(proyectosUsuario.stream().mapToDouble(Proyecto::getRecaudado).sum());

        List<Producto> productosUsuario = productoService.findProductosByUsuarioDonador(id);
        resp.setProductosCreados(productosUsuario.size());
        resp.setProductosDisponibles(productosUsuario.stream().filter(p -> p.getEstado() == EstadoProducto.DISPONIBLE).count());

        List<Producto> productosSolicitados = productoService.findProductosSolicitadosPorUsuario(id);
        resp.setProductosSolicitadosPorUsuario(productosSolicitados.size());

        resp.setNotificacionesNoLeidas(notificacionesService.getNotSeenNotificationFromUser(id).size());

        resp.setMensajesEnviados(mensajesService.countMessagesByUsuario(id));

        return ResponseEntity.ok(resp);
    }
}

