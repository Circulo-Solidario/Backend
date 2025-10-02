package com.backend.Backend.repositories;

import com.backend.Backend.models.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId")
    List<Producto> findListaDeProductosByCategoria(@Param("categoriaId") Long categoriaId);
}
