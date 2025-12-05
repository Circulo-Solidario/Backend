package com.backend.Backend.services;

import com.backend.Backend.models.Producto;
import com.backend.Backend.repositories.ProductoRepository;
import com.backend.Backend.utils.GeoLocationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public Page<Producto> findByFilters(String nombre, Long categoriaId, Double latitud, Double longitud, Integer distanciaKM, Pageable pageable) {
        Specification<Producto> spec = (root, query, cb) -> null;

        if(latitud != null && longitud != null && distanciaKM != null) {
            try {
                // Validar que las coordenadas sean válidas
                if(GeoLocationUtils.sonCoordenadasValidas(latitud, longitud)) {
                    // Calcular el bounding box basado en la distancia
                    GeoLocationUtils.BoundingBox boundingBox = GeoLocationUtils.calcularBoundingBox(latitud, longitud, distanciaKM);

                    // Crear una especificación que filtre productos cuya latitud y longitud estén dentro del bounding box
                    Specification<Producto> geoSpec = (root2, query2, cb2) -> cb2.and(
                        cb2.greaterThanOrEqualTo(root2.get("latitud"),boundingBox.getLatMin()),
                        cb2.lessThanOrEqualTo(root2.get("latitud"), boundingBox.getLatMax()),
                        cb2.greaterThanOrEqualTo(root2.get("longitud"), boundingBox.getLonMin()),
                        cb2.lessThanOrEqualTo(root2.get("longitud"),boundingBox.getLonMax())
                    );
                    spec = spec.and(geoSpec);
                }
            } catch (NumberFormatException e) {
                // Si las coordenadas no son válidas, se ignora el filtro de ubicación
                throw new IllegalArgumentException("Las coordenadas de latitud y longitud deben ser números válidos", e);
            }
        }

        if(nombre != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }
        if(categoriaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId));
        }
        return productoRepository.findAll(spec, pageable);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> findAllByCategoria(Long categoriaId) {
        return productoRepository.findListaDeProductosByCategoria(categoriaId);
    }

    public List<Producto> findProductosByUsuarioDonador(Long usuarioId) {
        return productoRepository.findAllByUsuarioId(usuarioId);
    }

    public List<Producto> findProductosSolicitadosPorUsuario(Long usuarioId) {
        return productoRepository.findAllBySolicitantesId(usuarioId);
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
}
