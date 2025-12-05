package com.backend.Backend.controllers;

import com.backend.Backend.dtos.estadistica.*;
import com.backend.Backend.models.*;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.backend.Backend.services.EstadisticasService;
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

    private final EstadisticasService estadisticasService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final ProyectoService proyectoService;
    private final NotificacionesService notificacionesService;
    private final MensajesService mensajesService;

    @GetMapping
    public ResponseEntity<EstadisticasGlobalesResponse> getEstadisticas() {
        return ResponseEntity.ok(estadisticasService.obtenerEstadisticasGlobales());
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

    @GetMapping("/organizacion/{id}/proyectos")
    public ResponseEntity<OrganizacionProyectosMetricsResponse> getMetricsForOrganizacion(@PathVariable Long id) {

    List<Proyecto> proyectosOrg = proyectoService.getProyectos(id, null, null);

    OrganizacionProyectosMetricsResponse resp = new OrganizacionProyectosMetricsResponse();
    resp.setOrganizacionId(id);
    resp.setTotalProyectos(proyectosOrg.size());

    double totalRecaudado = proyectosOrg.stream()
        .mapToDouble(p -> p.getRecaudado() == null ? 0.0 : p.getRecaudado())
        .sum();
    resp.setTotalRecaudado(totalRecaudado);

    // promedio de avance: promedio de (recaudado/objetivo)
    double promedioAvance = proyectosOrg.stream()
        .mapToDouble(p -> {
            Double objetivo = p.getObjetivo();
            Double recaudado = p.getRecaudado();
            if (objetivo == null || objetivo == 0.0 || recaudado == null) return 0.0;
            return (recaudado / objetivo) * 100.0;
        }).average().orElse(0.0);
    resp.setPromedioAvancePorcentaje(promedioAvance);

    resp.setProyectosActivos(proyectosOrg.stream().filter(p -> p.getEstado() == EstadoProyecto.ACTIVO).count());
    resp.setProyectosCancelados(proyectosOrg.stream().filter(p -> p.getEstado() == EstadoProyecto.CANCELADO).count());
    resp.setProyectosFinalizadosExitosos(proyectosOrg.stream().filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_EXITOSO).count());
    resp.setProyectosFinalizadosNoExitosos(proyectosOrg.stream().filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_NO_EXITOSO).count());

    // Top proyectos por recaudación (max 5)
    List<OrganizacionProyectoResumen> top = proyectosOrg.stream()
        .sorted((a, b) -> Double.compare(
            b.getRecaudado() == null ? 0.0 : b.getRecaudado(),
            a.getRecaudado() == null ? 0.0 : a.getRecaudado()))
        .limit(5)
        .map(p -> new OrganizacionProyectoResumen(
            p.getId(),
            p.getNombre(),
            p.getRecaudado() == null ? 0.0 : p.getRecaudado(),
            p.getObjetivo() == null ? 0.0 : p.getObjetivo(),
            (p.getObjetivo() == null || p.getObjetivo() == 0.0) ? 0.0 : ((p.getRecaudado() == null ? 0.0 : p.getRecaudado()) / p.getObjetivo()) * 100.0
        ))
        .toList();

    resp.setTopProyectosPorRecaudacion(top);

    return ResponseEntity.ok(resp);
    }
}

