package com.backend.Backend.services;

import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.backend.Backend.repositories.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;

    private Proyecto actualizarProyecto(Proyecto proyecto) {
        if (proyecto.getEstado() == EstadoProyecto.ACTIVO &&
                proyecto.getFechaFin().before(new Date())) {

            EstadoProyecto nuevoEstado =
                    proyecto.getRecaudado().compareTo(proyecto.getObjetivo()) >= 0
                            ? EstadoProyecto.FINALIZADO_EXITOSO
                            : EstadoProyecto.FINALIZADO_NO_EXITOSO;

            if (proyecto.getEstado() != nuevoEstado) {
                proyecto.setEstado(nuevoEstado);
                return proyectoRepository.save(proyecto);
            }
        }
        return proyecto;
    }

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> getProyectos(Long idOrganizacion, EstadoProyecto estado, String nombre) {
        Specification<Proyecto> spec = (root, query, cb) -> null;

        if (idOrganizacion != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("organizacion").get("id"), idOrganizacion));
        }

        if (estado != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("estado"), estado));
        }

        if(nombre != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("nombre"), "%" + nombre + "%"));
        }

        List<Proyecto> proyectos = proyectoRepository.findAll(spec);

        return proyectos.stream().map(this::actualizarProyecto).toList();
    }

    public Optional<Proyecto> getProyecto(Long id) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(id);
        return proyecto.map(this::actualizarProyecto);
    }

    public void deleteById(Long id) {
        proyectoRepository.deleteById(id);
    }

    public Proyecto actualizarRecaudado(Proyecto proyecto, Double recaudado){
        proyecto.setRecaudado(proyecto.getRecaudado() + recaudado);
        if (proyecto.getEstado() == EstadoProyecto.ACTIVO &&
                proyecto.getRecaudado() >= proyecto.getObjetivo()) {
            proyecto.setEstado(EstadoProyecto.FINALIZADO_EXITOSO);
        }
        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> getAllProyectos() {
        return proyectoRepository.findAll();
    }
}
