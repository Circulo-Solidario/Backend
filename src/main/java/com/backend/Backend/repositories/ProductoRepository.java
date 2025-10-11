package com.backend.Backend.repositories;

import com.backend.Backend.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId")
    List<Producto> findListaDeProductosByCategoria(@Param("categoriaId") Long categoriaId);
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);
    Page<Producto> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId, Pageable pageable);
    
    @Query("SELECT p FROM Producto p, Usuario u WHERE p.idUsuario = :usuarioId AND p.idUsuario = u.id AND EXISTS (SELECT r FROM u.roles r WHERE r.name = 'DONANTE')")
    List<Producto> findByUsuarioDonante(@Param("usuarioId") Long usuarioId);
}
