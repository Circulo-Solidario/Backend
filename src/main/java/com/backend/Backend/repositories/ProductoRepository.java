package com.backend.Backend.repositories;

import com.backend.Backend.models.Producto;
import com.backend.Backend.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT s FROM Producto s WHERE s.categoria = :categoria")
    List<Producto> findListaDeProductosByCategoria(@Param("categoria") String categoria);
}
