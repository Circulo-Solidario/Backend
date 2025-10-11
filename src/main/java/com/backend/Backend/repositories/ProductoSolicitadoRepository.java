package com.backend.Backend.repositories;

import com.backend.Backend.models.ProductoSolicitado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoSolicitadoRepository extends JpaRepository<ProductoSolicitado, Long> {
}
