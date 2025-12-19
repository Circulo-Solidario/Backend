package com.backend.Backend.services;

import com.backend.Backend.dtos.estadistica.*;
import com.backend.Backend.models.*;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.repositories.DonacionRepository;
import com.backend.Backend.repositories.PuntoRepository;
import com.backend.Backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadisticasService {
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final ProyectoService proyectoService;
    private final PuntoRepository puntoRepository;
    private final DonacionRepository donacionRepository;
    private final UsuarioRepository usuarioRepository;

    public EstadisticasGlobalesResponse obtenerEstadisticasGlobales() {
        EstadisticasGlobalesResponse response = new EstadisticasGlobalesResponse();

        // NODO USUARIOS
        response.setUsuarios(construirNodoUsuarios());

        // NODO PRODUCTOS
        response.setProductos(construirNodoProductos());

        // NODO PROYECTOS SOLIDARIOS
        response.setProyectosSolidarios(construirNodoProyectos());

        // NODO PUNTOS
        response.setPuntos(construirNodoPuntos());

        return response;
    }

    private UsuariosEstadisticaNode construirNodoUsuarios() {
        UsuariosEstadisticaNode node = new UsuariosEstadisticaNode();
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        // Cantidad de usuarios registrados
        node.setCantidadRegistrados(usuarios.size() - 1);

        // Cantidad por tipo (USUARIO, ORGANIZACION)
        Map<String, Long> cantidadPorTipo = new HashMap<>();
        cantidadPorTipo.put("USUARIO", usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.USUARIO).count());
        cantidadPorTipo.put("ORGANIZACION", usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.ORGANIZACION).count());
        node.setCantidadPorTipo(cantidadPorTipo);

        // Usuarios tipo USUARIO por rol
        Map<String, Long> usuariosUserPorRol = new HashMap<>();
        List<Usuario> usuariosDelTipo = usuarios.stream()
                .filter(u -> u.getTipoUsuario() == TipoUsuario.USUARIO)
                .toList();

        for (Usuario usuario : usuariosDelTipo) {
            if (usuario.getRoles() != null) {
                for (Rol rol : usuario.getRoles()) {
                    usuariosUserPorRol.put(
                            rol.getNombre(),
                            usuariosUserPorRol.getOrDefault(rol.getNombre(), 0L) + 1
                    );
                }
            }
        }
        node.setUsuariosUserPorRol(usuariosUserPorRol);

        return node;
    }

    private ProductosEstadisticaNode construirNodoProductos() {
        ProductosEstadisticaNode node = new ProductosEstadisticaNode();
        List<Producto> productos = productoService.getAllProductos();

        // Cantidad de productos publicados
        node.setCantidadPublicados(productos.size());

        // Cantidad por estado
        Map<String, Long> cantidadPorEstado = new HashMap<>();
        cantidadPorEstado.put("DISPONIBLE", productos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProducto.DISPONIBLE).count());
        cantidadPorEstado.put("SOLICITADO", productos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProducto.SOLICITADO).count());
        cantidadPorEstado.put("RESERVADO", productos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProducto.RESERVADO).count());
        cantidadPorEstado.put("ENTREGADO", productos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProducto.ENTREGADO).count());
        node.setCantidadPorEstado(cantidadPorEstado);

        // Cantidad por categoría
        Map<String, Long> cantidadPorCategoria = productos.stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().getNombre() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getCategoria().getNombre(),
                        Collectors.counting()
                ));
        node.setCantidadPorCategoria(cantidadPorCategoria);

        // Top 5 usuarios con más productos entregados
        Map<Long, Long> productosEntregadosPorUsuario = productos.stream()
                .filter(p -> p.getEstado() == EstadoProducto.ENTREGADO && p.getUsuario() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getUsuario().getId(),
                        Collectors.counting()
                ));

        List<UsuarioProductosEntregadosResumen> topUsuarios = productosEntregadosPorUsuario.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(entry -> {
                    Optional<Usuario> usuario = usuarioRepository.findById(entry.getKey());
                    return new UsuarioProductosEntregadosResumen(
                            entry.getKey(),
                            usuario.map(Usuario::getNombreApellido).orElse("Usuario desconocido"),
                            entry.getValue()
                    );
                })
                .toList();
        node.setTopUsuariosConMasEntregados(topUsuarios);

        return node;
    }

    private ProyectosSolidariosEstadisticaNode construirNodoProyectos() {
        ProyectosSolidariosEstadisticaNode node = new ProyectosSolidariosEstadisticaNode();
        List<Proyecto> proyectos = proyectoService.getAllProyectos();
        List<Donacion> donaciones = donacionRepository.findAll();

        // Cantidad de proyectos publicados
        node.setCantidadPublicados(proyectos.size());

        // Total recaudado
        double totalRecaudado = proyectos.stream()
                .filter(p -> p.getRecaudado() != null && p.getRecaudado() > 0)
                .mapToDouble(Proyecto::getRecaudado)
                .sum();
        node.setTotalRecaudado(totalRecaudado);

        // Cantidad por estado
        Map<String, Long> cantidadPorEstado = new HashMap<>();
        cantidadPorEstado.put("ACTIVO", proyectos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProyecto.ACTIVO).count());
        cantidadPorEstado.put("CANCELADO", proyectos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProyecto.CANCELADO).count());
        cantidadPorEstado.put("FINALIZADO_EXITOSO", proyectos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProyecto.FINALIZADO_EXITOSO).count());
        cantidadPorEstado.put("FINALIZADO_NO_EXITOSO", proyectos.stream().filter(p -> p.getEstado() != null && p.getEstado() == EstadoProyecto.FINALIZADO_NO_EXITOSO).count());
        node.setCantidadPorEstado(cantidadPorEstado);

        // Top 5 usuarios con más donaciones totales
        Map<Long, Double> donacionesPorUsuario = donaciones.stream()
                .filter(d -> d.getDonador() != null && d.getMonto() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getDonador().getId(),
                        Collectors.summingDouble(Donacion::getMonto)
                ));

        List<UsuarioDonacionesResumen> topDonadores = donacionesPorUsuario.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(entry -> {
                    Optional<Usuario> usuario = usuarioRepository.findById(entry.getKey());
                    return new UsuarioDonacionesResumen(
                            entry.getKey(),
                            usuario.map(Usuario::getNombreApellido).orElse("Usuario desconocido"),
                            entry.getValue()
                    );
                })
                .toList();
        node.setTopUsuariosConMasDonaciones(topDonadores);

        // Monto recaudado por día del mes actual
        Calendar now = Calendar.getInstance();
        int mesActual = now.get(Calendar.MONTH);
        int anioActual = now.get(Calendar.YEAR);

        Map<String, Double> recaudoPorDia = donaciones.stream()
                .filter(d -> d.getFechaDonacion() != null && d.getMonto() != null && d.getMonto() > 0)
                .filter(d -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d.getFechaDonacion());
                    return cal.get(Calendar.MONTH) == mesActual && cal.get(Calendar.YEAR) == anioActual;
                })
                .collect(Collectors.groupingBy(
                        d -> {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            return sdf.format(d.getFechaDonacion());
                        },
                        Collectors.summingDouble(Donacion::getMonto)
                ));
        node.setRecaudoPorDia(recaudoPorDia);

        return node;
    }

    private PuntosEstadisticaNode construirNodoPuntos() {
        PuntosEstadisticaNode node = new PuntosEstadisticaNode();
        List<Punto> puntos = puntoRepository.findAll();

        // Cantidad total
        node.setCantidadTotal(puntos.size());

        // Cantidad por estado
        Map<String, Long> cantidadPorEstado = puntos.stream()
                .filter(p -> p.getEstado() != null && !p.getEstado().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        Punto::getEstado,
                        Collectors.counting()
                ));
        node.setCantidadPorEstado(cantidadPorEstado);

        return node;
    }

    public EstadisticasPersonalesResponse obtenerEstadisticasPersonales(Long usuarioId) {
        EstadisticasPersonalesResponse response = new EstadisticasPersonalesResponse();

        // NODO PRODUCTOS
        response.setProductos(construirNodoProductosPersonales(usuarioId));

        // NODO PERSONAS EN SITUACIÓN DE CALLE
        response.setPersonasEnSituacionDeCalle(construirNodoPersonasEnSituacionDeCalle(usuarioId));

        // NODO DONACIONES
        response.setDonaciones(construirNodoDonacionesPersonales(usuarioId));

        return response;
    }

    private ProductosPersonalesEstadisticaNode construirNodoProductosPersonales(Long usuarioId) {
        ProductosPersonalesEstadisticaNode node = new ProductosPersonalesEstadisticaNode();

        List<Producto> productos = productoService.getProductosByUsuarioId(usuarioId);

        // Cantidad de productos publicados
        node.setCantidadPublicados(productos.size());

        // Cantidad de productos donados
        node.setCantidadDonados(
                (int) productos.stream().filter(p -> p.getEstado() == EstadoProducto.ENTREGADO).count()
        );

        // Cantidad por estado
        Map<String, Long> cantidadPorEstado = productos.stream()
                .filter(p -> p.getEstado() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getEstado().name(),
                        Collectors.counting()
                ));

        for (EstadoProducto estado : EstadoProducto.values()) {
            cantidadPorEstado.putIfAbsent(estado.name(), 0L);
        }

        node.setCantidadPorEstado(cantidadPorEstado);

        // Cantidad de productos solicitados
        node.setCantidadSolicitados(
                (int) productos.stream().filter(p -> !p.getSolicitantes().isEmpty()).count()
        );

        // Cantidad de productos recibidos
        node.setCantidadRecibidos(
                (int) productos.stream().filter(p -> p.getUsuario() != null && p.getUsuario().getId().equals(usuarioId)).count()
        );

        // Cantidad de cada categoría solicitada
        Map<String, Long> cantidadPorCategoriaSolicitada = productos.stream()
                .filter(p -> !p.getSolicitantes().isEmpty() && p.getCategoria() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getCategoria().getNombre(),
                        Collectors.counting()
                ));
        node.setCantidadPorCategoriaSolicitada(cantidadPorCategoriaSolicitada);

        return node;
    }

    private PersonasEnSituacionDeCalleEstadisticaNode construirNodoPersonasEnSituacionDeCalle(Long usuarioId) {
        PersonasEnSituacionDeCalleEstadisticaNode node = new PersonasEnSituacionDeCalleEstadisticaNode();

        List<Punto> puntos = puntoRepository.findAll();

        // Cantidad de puntos registrados por el usuario
        node.setCantidadPuntosRegistrados(
                (int) puntos.stream().filter(p -> p.getUsuarioRegistro() != null && p.getUsuarioRegistro().getId().equals(usuarioId)).count()
        );

        // Cantidad de puntos donde el usuario ayudó
        node.setCantidadPuntosAyudados(
                (int) puntos.stream().filter(p -> p.getUsuarioAyudo() != null && p.getUsuarioAyudo().getId().equals(usuarioId)).count()
        );

        // Orden del usuario por puntos ayudados
        Map<Long, Long> puntosAyudadosPorUsuario = puntos.stream()
                .filter(p -> p.getUsuarioAyudo() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getUsuarioAyudo().getId(),
                        Collectors.counting()
                ));

        List<Long> ranking = puntosAyudadosPorUsuario.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        node.setOrdenUsuario(ranking.indexOf(usuarioId) + 1);

        return node;
    }

    private DonacionesPersonalesEstadisticaNode construirNodoDonacionesPersonales(Long usuarioId) {
        DonacionesPersonalesEstadisticaNode node = new DonacionesPersonalesEstadisticaNode();

        List<Donacion> donaciones = donacionRepository.findAllByDonadorId(usuarioId);

        // Cantidad donada por el usuario
        node.setCantidadDonada(
                donaciones.stream().mapToDouble(Donacion::getMonto).sum()
        );

        // Donación más alta hecha por el usuario
        node.setDonacionMasAlta(
                donaciones.stream().mapToDouble(Donacion::getMonto).max().orElse(0)
        );

        // Organización a la que más ayudó
        Optional<Map.Entry<String, Double>> organizacionMasAyudada = donaciones.stream()
                .filter(d -> d.getProyecto() != null && d.getProyecto().getOrganizacion() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getProyecto().getOrganizacion().getNombreApellido(),
                        Collectors.summingDouble(Donacion::getMonto)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (organizacionMasAyudada.isPresent()) {
            Map<String, Object> organizacionMasAyudadaNodo = new HashMap<>();
            organizacionMasAyudadaNodo.put("nombreOrganizacion", organizacionMasAyudada.get().getKey());
            organizacionMasAyudadaNodo.put("monto", organizacionMasAyudada.get().getValue());
            node.setOrganizacionMasAyudada(organizacionMasAyudadaNodo);
        }

        // Listado de donaciones
        List<DonacionDetalle> detalles = donaciones.stream()
                .map(d -> new DonacionDetalle(
                        d.getProyecto() != null && d.getProyecto().getOrganizacion() != null ? d.getProyecto().getOrganizacion().getNombreApellido() : "N/A",
                        d.getFechaDonacion(),
                        d.getMonto()
                ))
                .toList();
        node.setDonaciones(detalles);

        return node;
    }

    /**
     * Obtiene estadísticas completas de una organización
     * @param organizacionId ID de la organización
     * @return Estadísticas completas incluyendo proyectos, donaciones y donadores
     */
    public OrganizacionEstadisticaResponse obtenerEstadisticasOrganizacion(Long organizacionId) {
        OrganizacionEstadisticaResponse response = new OrganizacionEstadisticaResponse();

        // Obtener organización
        Optional<Usuario> organizacion = usuarioRepository.findById(organizacionId);
        if (organizacion.isEmpty()) {
            throw new RuntimeException("Organización no encontrada");
        }

        response.setOrganizacionId(organizacionId);
        response.setNombreOrganizacion(organizacion.get().getNombreApellido());

        // Obtener proyectos de la organización
        List<Proyecto> proyectos = proyectoService.getProyectos(organizacionId, null, null);

        // Obtener todas las donaciones de los proyectos de esta organización
        List<Donacion> donaciones = donacionRepository.findAll().stream()
                .filter(d -> d.getProyecto() != null &&
                        d.getProyecto().getOrganizacion() != null &&
                        d.getProyecto().getOrganizacion().getId().equals(organizacionId))
                .toList();

        // Construir métricas
        response.setProyectos(construirProyectosMetrics(proyectos, donaciones));
        response.setDonaciones(construirDonacionesMetrics(donaciones));
        response.setDonadores(construirDonadoresMetrics(donaciones));
        response.setRecaudacionPorMes(construirRecaudacionPorMes(donaciones));
        response.setDonacionesPorMes(construirDonacionesPorMes(donaciones));

        return response;
    }

    private OrganizacionEstadisticaResponse.ProyectosMetrics construirProyectosMetrics(
            List<Proyecto> proyectos, List<Donacion> donaciones) {

        OrganizacionEstadisticaResponse.ProyectosMetrics metrics =
                new OrganizacionEstadisticaResponse.ProyectosMetrics();

        metrics.setTotalProyectos(proyectos.size());
        metrics.setProyectosActivos(proyectos.stream()
                .filter(p -> p.getEstado() == EstadoProyecto.ACTIVO).count());
        metrics.setProyectosCancelados(proyectos.stream()
                .filter(p -> p.getEstado() == EstadoProyecto.CANCELADO).count());
        metrics.setProyectosFinalizadosExitosos(proyectos.stream()
                .filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_EXITOSO).count());
        metrics.setProyectosFinalizadosNoExitosos(proyectos.stream()
                .filter(p -> p.getEstado() == EstadoProyecto.FINALIZADO_NO_EXITOSO).count());

        double totalRecaudado = proyectos.stream()
                .mapToDouble(p -> p.getRecaudado() == null ? 0.0 : p.getRecaudado())
                .sum();
        metrics.setTotalRecaudado(totalRecaudado);

        double totalObjetivo = proyectos.stream()
                .mapToDouble(p -> p.getObjetivo() == null ? 0.0 : p.getObjetivo())
                .sum();
        metrics.setTotalObjetivo(totalObjetivo);

        double promedioAvance = proyectos.stream()
                .mapToDouble(p -> {
                    if (p.getObjetivo() == null || p.getObjetivo() == 0.0 || p.getRecaudado() == null)
                        return 0.0;
                    return (p.getRecaudado() / p.getObjetivo()) * 100.0;
                }).average().orElse(0.0);
        metrics.setPromedioAvancePorcentaje(promedioAvance);

        // Tasa de éxito
        long totalFinalizados = metrics.getProyectosFinalizadosExitosos() +
                metrics.getProyectosFinalizadosNoExitosos();
        double tasaExito = totalFinalizados > 0
                ? (metrics.getProyectosFinalizadosExitosos() * 100.0) / totalFinalizados
                : 0.0;
        metrics.setTasaExito(tasaExito);

        // Top proyectos por recaudación
        List<OrganizacionEstadisticaResponse.ProyectoResumen> topProyectos = proyectos.stream()
                .sorted((a, b) -> Double.compare(
                        b.getRecaudado() == null ? 0.0 : b.getRecaudado(),
                        a.getRecaudado() == null ? 0.0 : a.getRecaudado()))
                .limit(5)
                .map(p -> {
                    long totalDonacionesProyecto = donaciones.stream()
                            .filter(d -> d.getProyecto() != null && d.getProyecto().getId().equals(p.getId()))
                            .count();
                    return new OrganizacionEstadisticaResponse.ProyectoResumen(
                            p.getId(),
                            p.getNombre(),
                            p.getRecaudado() == null ? 0.0 : p.getRecaudado(),
                            p.getObjetivo() == null ? 0.0 : p.getObjetivo(),
                            (p.getObjetivo() == null || p.getObjetivo() == 0.0) ? 0.0
                                    : ((p.getRecaudado() == null ? 0.0 : p.getRecaudado()) / p.getObjetivo()) * 100.0,
                            p.getEstado() != null ? p.getEstado().name() : "DESCONOCIDO",
                            totalDonacionesProyecto
                    );
                })
                .toList();
        metrics.setTopProyectosPorRecaudacion(topProyectos);

        // Proyectos recientes (últimos 5)
        List<OrganizacionEstadisticaResponse.ProyectoResumen> proyectosRecientes = proyectos.stream()
                .sorted((a, b) -> {
                    if (a.getFechaInicio() == null && b.getFechaInicio() == null) return 0;
                    if (a.getFechaInicio() == null) return 1;
                    if (b.getFechaInicio() == null) return -1;
                    return b.getFechaInicio().compareTo(a.getFechaInicio());
                })
                .limit(5)
                .map(p -> {
                    long totalDonacionesProyecto = donaciones.stream()
                            .filter(d -> d.getProyecto() != null && d.getProyecto().getId().equals(p.getId()))
                            .count();
                    return new OrganizacionEstadisticaResponse.ProyectoResumen(
                            p.getId(),
                            p.getNombre(),
                            p.getRecaudado() == null ? 0.0 : p.getRecaudado(),
                            p.getObjetivo() == null ? 0.0 : p.getObjetivo(),
                            (p.getObjetivo() == null || p.getObjetivo() == 0.0) ? 0.0
                                    : ((p.getRecaudado() == null ? 0.0 : p.getRecaudado()) / p.getObjetivo()) * 100.0,
                            p.getEstado() != null ? p.getEstado().name() : "DESCONOCIDO",
                            totalDonacionesProyecto
                    );
                })
                .toList();
        metrics.setProyectosRecientes(proyectosRecientes);

        return metrics;
    }

    private OrganizacionEstadisticaResponse.DonacionesMetrics construirDonacionesMetrics(
            List<Donacion> donaciones) {

        OrganizacionEstadisticaResponse.DonacionesMetrics metrics =
                new OrganizacionEstadisticaResponse.DonacionesMetrics();

        metrics.setTotalDonaciones(donaciones.size());

        double montoTotal = donaciones.stream()
                .mapToDouble(d -> d.getMonto() == null ? 0.0 : d.getMonto())
                .sum();
        metrics.setMontoTotalRecaudado(montoTotal);

        double donacionPromedio = donaciones.isEmpty() ? 0.0 : montoTotal / donaciones.size();
        metrics.setDonacionPromedio(donacionPromedio);

        metrics.setDonacionMasAlta(donaciones.stream()
                .mapToDouble(d -> d.getMonto() == null ? 0.0 : d.getMonto())
                .max().orElse(0.0));

        metrics.setDonacionMasBaja(donaciones.stream()
                .filter(d -> d.getMonto() != null && d.getMonto() > 0)
                .mapToDouble(Donacion::getMonto)
                .min().orElse(0.0));

        // Recaudación último mes
        Calendar now = Calendar.getInstance();
        int mesActual = now.get(Calendar.MONTH);
        int anioActual = now.get(Calendar.YEAR);

        double recaudacionMesActual = donaciones.stream()
                .filter(d -> d.getFechaDonacion() != null && d.getMonto() != null)
                .filter(d -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d.getFechaDonacion());
                    return cal.get(Calendar.MONTH) == mesActual &&
                            cal.get(Calendar.YEAR) == anioActual;
                })
                .mapToDouble(Donacion::getMonto)
                .sum();
        metrics.setRecaudacionUltimoMes(recaudacionMesActual);

        // Recaudación mes anterior
        Calendar mesAnterior = Calendar.getInstance();
        mesAnterior.add(Calendar.MONTH, -1);
        int mesAnt = mesAnterior.get(Calendar.MONTH);
        int anioAnt = mesAnterior.get(Calendar.YEAR);

        double recaudacionMesAnterior = donaciones.stream()
                .filter(d -> d.getFechaDonacion() != null && d.getMonto() != null)
                .filter(d -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d.getFechaDonacion());
                    return cal.get(Calendar.MONTH) == mesAnt &&
                            cal.get(Calendar.YEAR) == anioAnt;
                })
                .mapToDouble(Donacion::getMonto)
                .sum();
        metrics.setRecaudacionMesAnterior(recaudacionMesAnterior);

        // Tasa de crecimiento
        double tasaCrecimiento = recaudacionMesAnterior > 0
                ? ((recaudacionMesActual - recaudacionMesAnterior) / recaudacionMesAnterior) * 100.0
                : 0.0;
        metrics.setTasaCrecimiento(tasaCrecimiento);

        return metrics;
    }

    private OrganizacionEstadisticaResponse.DonadoresMetrics construirDonadoresMetrics(
            List<Donacion> donaciones) {

        OrganizacionEstadisticaResponse.DonadoresMetrics metrics =
                new OrganizacionEstadisticaResponse.DonadoresMetrics();

        // Donadores únicos
        Set<Long> donadoresUnicos = donaciones.stream()
                .filter(d -> d.getDonador() != null)
                .map(d -> d.getDonador().getId())
                .collect(Collectors.toSet());
        metrics.setTotalDonadoresUnicos(donadoresUnicos.size());

        // Donadores recurrentes (más de una donación)
        Map<Long, Long> donacionesPorDonador = donaciones.stream()
                .filter(d -> d.getDonador() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getDonador().getId(),
                        Collectors.counting()
                ));

        long donadoresRecurrentes = donacionesPorDonador.values().stream()
                .filter(count -> count > 1)
                .count();
        metrics.setDonadoresRecurrentes(donadoresRecurrentes);

        // Promedio de retención
        double promedioRetencion = donadoresUnicos.size() > 0
                ? (donadoresRecurrentes * 100.0) / donadoresUnicos.size()
                : 0.0;
        metrics.setPromedioRetencion(promedioRetencion);

        // Top donadores
        Map<Long, Double> montoPorDonador = donaciones.stream()
                .filter(d -> d.getDonador() != null && d.getMonto() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getDonador().getId(),
                        Collectors.summingDouble(Donacion::getMonto)
                ));

        List<OrganizacionEstadisticaResponse.DonadorResumen> topDonadores = montoPorDonador.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> {
                    Optional<Usuario> donador = usuarioRepository.findById(entry.getKey());
                    long cantidadDonaciones = donacionesPorDonador.getOrDefault(entry.getKey(), 0L);
                    return new OrganizacionEstadisticaResponse.DonadorResumen(
                            entry.getKey(),
                            donador.map(Usuario::getNombreApellido).orElse("Usuario desconocido"),
                            entry.getValue(),
                            cantidadDonaciones
                    );
                })
                .toList();
        metrics.setTopDonadores(topDonadores);

        // Donaciones por rango
        Map<String, Long> donacionesPorRango = new HashMap<>();
        donacionesPorRango.put("0-100", donaciones.stream()
                .filter(d -> d.getMonto() != null && d.getMonto() > 0 && d.getMonto() <= 100).count());
        donacionesPorRango.put("100-500", donaciones.stream()
                .filter(d -> d.getMonto() != null && d.getMonto() > 100 && d.getMonto() <= 500).count());
        donacionesPorRango.put("500-1000", donaciones.stream()
                .filter(d -> d.getMonto() != null && d.getMonto() > 500 && d.getMonto() <= 1000).count());
        donacionesPorRango.put("1000+", donaciones.stream()
                .filter(d -> d.getMonto() != null && d.getMonto() > 1000).count());
        metrics.setDonacionesPorRango(donacionesPorRango);

        return metrics;
    }

    private Map<String, Double> construirRecaudacionPorMes(List<Donacion> donaciones) {
        Map<String, Double> recaudacionPorMes = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar cal = Calendar.getInstance();
        // Últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            Calendar tempCal = (Calendar) cal.clone();
            tempCal.add(Calendar.MONTH, -i);
            String mesKey = sdf.format(tempCal.getTime());

            double recaudacion = donaciones.stream()
                    .filter(d -> d.getFechaDonacion() != null && d.getMonto() != null)
                    .filter(d -> sdf.format(d.getFechaDonacion()).equals(mesKey))
                    .mapToDouble(Donacion::getMonto)
                    .sum();

            recaudacionPorMes.put(mesKey, recaudacion);
        }

        return recaudacionPorMes;
    }

    private Map<String, Long> construirDonacionesPorMes(List<Donacion> donaciones) {
        Map<String, Long> donacionesPorMes = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar cal = Calendar.getInstance();
        // Últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            Calendar tempCal = (Calendar) cal.clone();
            tempCal.add(Calendar.MONTH, -i);
            String mesKey = sdf.format(tempCal.getTime());

            long cantidad = donaciones.stream()
                    .filter(d -> d.getFechaDonacion() != null)
                    .filter(d -> sdf.format(d.getFechaDonacion()).equals(mesKey))
                    .count();

            donacionesPorMes.put(mesKey, cantidad);
        }

        return donacionesPorMes;
    }
}
