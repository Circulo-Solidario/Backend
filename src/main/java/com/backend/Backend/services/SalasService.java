package com.backend.Backend.services;

import com.backend.Backend.models.Sala;
import com.backend.Backend.repositories.SalasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalasService {
    private final SalasRepository salasRepository;

    public Sala save(Sala sala) {
        return salasRepository.save(sala);
    }

    public Optional<Sala> findSalaBySolicitudId(Long solicitudId){
        return salasRepository.findBySolicitudId(solicitudId);
    }

    public List<Sala> findSalaBySolicitanteId(Long solicitanteId){
        return salasRepository.findAllBySolicitudSolicitanteId(solicitanteId);
    }

    public List<Sala> findSalaByDondadorId(Long dondadorId){
        return salasRepository.findAllBySolicitudDonadorId(dondadorId);
    }

    public Optional<Sala> findSalaById(Long id){
        return salasRepository.findById(id);
    }
}
