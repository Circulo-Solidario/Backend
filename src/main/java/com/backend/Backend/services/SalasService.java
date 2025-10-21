package com.backend.Backend.services;

import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Sala;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.repositories.SalasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalasService {
    private final SalasRepository salasRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;


    public String searchRoom(Long user1, Long user2, Long productId) {
        return salasRepository.getRoomFromUsers(user1, user2, productId);
    }

    public String createRoom(Long user1, Long user2, Long productId) {
        Usuario usuario1 = usuarioService.getUsuarioById(user1);
        Usuario usuario2 = usuarioService.getUsuarioById(user2);
        Producto producto = productoService.findById(productId);
        return salasRepository.save(new Sala(usuario1, usuario2, producto)).getNombreSala();
    }

    public Sala findRoomByName(String nombreSala){
        Optional<Sala> sala = salasRepository.findByNombreSala(nombreSala);
        return sala.orElse(null);
    }
}
