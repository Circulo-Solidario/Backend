package com.backend.Backend.services;

import com.backend.Backend.dtos.punto.EstadoRequest;
import com.backend.Backend.dtos.punto.PuntoRequest;
import com.backend.Backend.models.Punto;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.PuntoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PuntoService {
    private final PuntoRepository puntoRepository;
    private final UsuarioService usuarioService;

    public Punto save(Punto punto) {
        return puntoRepository.save(punto);
    }

    public Punto saveNewPunto(PuntoRequest request){
        Usuario usuarioRegistro = usuarioService.getUsuarioById(request.getUsuarioRegistroId()).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado")
        );
        Punto punto = Punto.builder()
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .descripcion(request.getDescripcion())
                .estado("pendiente")
                .usuarioRegistro(usuarioRegistro)
                .build();
        return puntoRepository.save(punto);
    }

    public List<Punto> findAll() {
        return puntoRepository.findAll();
    }

    public Punto findById(Long id) {
        return puntoRepository.findById(id).orElseThrow(() -> new RuntimeException("Punto no encontrado"));
    }

    public Punto actualizarEstado(Long id, EstadoRequest request) {
        Usuario usuarioAyudo = usuarioService.getUsuarioById(request.getUsuarioAyudoId()).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado")
        );
        Punto punto = findById(id);
        punto.setEstado(request.getEstado());
        punto.setUsuarioAyudo(usuarioAyudo);
        return puntoRepository.save(punto);
    }

    public void deleteById(Long id) {
        Punto punto = findById(id);
        puntoRepository.deleteById(punto.getId());
    }

}
