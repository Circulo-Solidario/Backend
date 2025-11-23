package com.backend.Backend.services;

import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Sala;
import com.backend.Backend.models.enums.EstadoProducto;
import com.backend.Backend.models.enums.EstadoSala;
import com.backend.Backend.models.enums.EstadoSolicitud;
import com.backend.Backend.repositories.SalasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalasService {
    private final SalasRepository salasRepository;
    private final SolicitudService solicitudService;
    private final ProductoService productoService;

    public Sala save(Sala sala) {
        return salasRepository.save(sala);
    }

    public Optional<Sala> findSalaBySolicitudId(Long solicitudId){
        return salasRepository.findBySolicitudId(solicitudId);
    }

    public List<Sala> findSalaBySolicitanteId(Long solicitanteId){
        return salasRepository.findAllBySolicitudSolicitanteIdAndEstado(solicitanteId, EstadoSala.PENDIENTE);
    }

    public List<Sala> findSalaByDondadorId(Long dondadorId){
        return salasRepository.findAllBySolicitudDonadorIdAndEstado(dondadorId, EstadoSala.PENDIENTE);
    }

    public Optional<Sala> findSalaById(Long id){
        return salasRepository.findById(id);
    }

    public Sala cambiarEstado(Sala sala, EstadoSala estado){
        sala.setEstado(estado);
        if(estado.equals(EstadoSala.RECHAZADA)){
            solicitudService.cambiarEstado(sala.getSolicitud().getId(), EstadoSolicitud.RECHAZADA);
        }
        if(estado.equals(EstadoSala.ENTREGADA)){
            Producto producto = sala.getSolicitud().getProducto();
            producto.setEstado(EstadoProducto.ENTREGADO);
            productoService.save(producto);
        }
        return salasRepository.save(sala);
    }
}
