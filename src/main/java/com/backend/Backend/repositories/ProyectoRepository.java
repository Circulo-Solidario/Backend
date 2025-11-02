package com.backend.Backend.repositories;

import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.enums.EstadoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long>, JpaSpecificationExecutor<Proyecto> {
    Optional<Proyecto> findById(Long id);
    List<Proyecto> findAllByEstado(EstadoProyecto estado);
}
