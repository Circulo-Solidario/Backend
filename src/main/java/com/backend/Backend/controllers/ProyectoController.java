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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<List<ProyectoResponse>> getProyectos(@RequestParam(required = false) Long idOrganizacion, @RequestParam(required = false) EstadoProyecto estado, @RequestParam(required = false) String nombre){
        if(idOrganizacion != null){
        Optional<Usuario> organizacion = usuarioService.getUsuarioById(idOrganizacion);
        if(organizacion.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        }
        return ResponseEntity.ok(proyectoService.getProyectos(idOrganizacion, estado, nombre).stream().map(proyectoMapper::entityToResponse).toList());
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> getProyecto(@PathVariable Long id){
        Optional<Proyecto> proyecto = proyectoService.getProyecto(id);
        return proyecto.map(value -> ResponseEntity.ok(proyectoMapper.entityToResponse(value))).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponse> updateProyecto(@PathVariable Long id, @RequestBody ProyectoRequest proyectoBody) {
        Optional<Proyecto> proyectoOptional = proyectoService.getProyecto(id);
        if (proyectoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proyecto proyecto = proyectoOptional.get();

        Optional<Usuario> organizacion = usuarioService.getUsuarioById(proyectoBody.getIdOrganizacion());
        if (organizacion.isEmpty() || organizacion.get().getTipoUsuario() != TipoUsuario.ORGANIZACION || proyectoBody.getImagenes() == null || proyectoBody.getImagenes().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Se actualizan todos los campos (menos recaudado, estado ni fechaInicio)
        proyecto.setNombre(proyectoBody.getNombre());
        proyecto.setDescripcion(proyectoBody.getDescripcion());
        proyecto.setOrganizacion(organizacion.get());
        proyecto.setObjetivo(proyectoBody.getObjetivo());
        proyecto.setFechaFin(proyectoBody.getFechaFin());
        proyecto.setEstado(proyecto.getEstado());

        List<ImagenesProyecto> imagenes = new ArrayList<>();
        proyectoBody.getImagenes().forEach(url -> {
            ImagenesProyecto imagen = new ImagenesProyecto();
            imagen.setUrlImagen(url);
            imagen.setProyecto(proyecto);
            imagenes.add(imagen);
        });
        proyecto.setImagenes(imagenes);

        Proyecto actualizado = proyectoService.save(proyecto);
        return ResponseEntity.ok(proyectoMapper.entityToResponse(actualizado));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.getProyecto(id);
        if (proyecto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        proyectoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
