package com.backend.Backend.services;

import com.backend.Backend.models.Proyecto;
import com.backend.Backend.repositories.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }


}
