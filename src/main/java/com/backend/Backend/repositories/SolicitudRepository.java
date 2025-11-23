package com.backend.Backend.repositories;

import com.backend.Backend.models.Solicitud;
import com.backend.Backend.models.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByProductoId(Long id);
    List<Solicitud> findAllBySolicitanteIdAndEstado(Long id, EstadoSolicitud estado);
    List<Solicitud> findAllByDonadorIdAndEstado(Long id, EstadoSolicitud estado);
    List<Solicitud> findAllByProductoIdAndEstadoNot(Long producto_id, EstadoSolicitud estado);
}
