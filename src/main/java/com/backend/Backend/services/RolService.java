package com.backend.Backend.services;

import com.backend.Backend.models.Rol;
import com.backend.Backend.repositories.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolesRepository rolesRepository;

    public List<Rol> obtenerRolesDesdeListaId(List<Long> ids) {
        return rolesRepository.findAllById(ids);
    }
}
