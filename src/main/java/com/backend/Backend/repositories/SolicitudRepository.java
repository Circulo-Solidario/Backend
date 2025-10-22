package com.backend.Backend.repositories;

import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Solicitud;
import com.backend.Backend.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByProductoId(Long id);
    List<Solicitud> findAllByDeUsuario_Id(Long id);
    List<Solicitud> findAllByAusuario_Id(Long id);
}
