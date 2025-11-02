package com.backend.Backend.services;

import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.backend.Backend.repositories.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;

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

        return proyectoRepository.findAll(spec);
    }

    public Optional<Proyecto> getProyecto(Long id) {
        return proyectoRepository.findById(id);
    }
}
