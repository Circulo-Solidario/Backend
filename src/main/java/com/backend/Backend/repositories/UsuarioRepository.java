package com.backend.Backend.repositories;

import com.backend.Backend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByActivo(Boolean activo);
}
