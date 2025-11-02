package com.backend.Backend.controllers;

import com.backend.Backend.dtos.proyecto.ProyectoRequest;
import com.backend.Backend.dtos.proyecto.ProyectoResponse;
import com.backend.Backend.mappers.ProyectoMapper;
import com.backend.Backend.models.ImagenesProyecto;
import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.Usuario;
import com.backend.Backend.models.enums.EstadoProyecto;
import com.backend.Backend.models.enums.TipoUsuario;
import com.backend.Backend.services.ProyectoService;
import com.backend.Backend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {
    private final ProyectoService proyectoService;
    private final UsuarioService usuarioService;
    private final ProyectoMapper proyectoMapper;

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<ProyectoResponse> createProyecto(@RequestBody ProyectoRequest proyectoBody) {
        Optional<Usuario> organizacion = usuarioService.getUsuarioById(proyectoBody.getIdOrganizacion());
        if (organizacion.isEmpty() || organizacion.get().getTipoUsuario() != TipoUsuario.ORGANIZACION || proyectoBody.getImagenes().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(proyectoBody.getNombre());
        proyecto.setDescripcion(proyectoBody.getDescripcion());
        proyecto.setOrganizacion(organizacion.get());
        proyecto.setObjetivo(proyectoBody.getObjetivo());
        proyecto.setFechaFin(proyectoBody.getFechaFin());
        proyecto.setEstado(EstadoProyecto.ACTIVO);
        proyecto.setRecaudado(0.0);
        proyecto.setFechaInicio(Date.from(Instant.now()));
        List<ImagenesProyecto> imagenes = new ArrayList<>();
        proyectoBody.getImagenes().forEach(url -> {
            ImagenesProyecto imagen = new ImagenesProyecto();
            imagen.setUrlImagen(url);
            imagen.setProyecto(proyecto);
            imagenes.add(imagen);
        });
        proyecto.setImagenes(imagenes);
        return ResponseEntity.ok(proyectoMapper.entityToResponse(proyectoService.save(proyecto)));
    }
}
