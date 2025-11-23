package com.backend.Backend.config;

import com.backend.Backend.models.Rol;
import com.backend.Backend.models.Categoria;
import com.backend.Backend.repositories.CategoriaRepository;
import com.backend.Backend.repositories.RolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RolesRepository rolRepository, CategoriaRepository categoriaRepository) {
        return args -> {
            // Solo inicializar si no existen roles
            if (rolRepository.count() == 0) {
                rolRepository.save(new Rol(null, "INVITADO", "Usuario regular", true));
                rolRepository.save(new Rol(null, "DONANTE", "Usuario donador", true));
                rolRepository.save(new Rol(null, "DONATARIO", "Usuario donatario", true));
                rolRepository.save(new Rol(null, "VOLUNTARIO OBSERVADOR", "Usuario voluntario", true));
                rolRepository.save(new Rol(null, "ADMINISTRADOR", "Administrador del sistema", true));
                System.out.println("✅ Roles insertados correctamente");
            }

            // Solo inicializar si no existen categorías
            if (categoriaRepository.count() == 0) {
                categoriaRepository.save(new Categoria(null, "Ropa", "Vestimenta y accesorios de todo tipo"));
                categoriaRepository.save(new Categoria(null, "Calzado", "Zapatos, zapatillas y todo tipo de calzado"));
                categoriaRepository.save(new Categoria(null, "Alimentos no perecederos", "Alimentos envasados y de larga duración"));
                categoriaRepository.save(new Categoria(null, "Artículos de higiene", "Productos de aseo personal y limpieza"));
                categoriaRepository.save(new Categoria(null, "Abrigos", "Frazadas, mantas y ropa de abrigo"));
                categoriaRepository.save(new Categoria(null, "Medicamentos", "Medicamentos y artículos de primeros auxilios"));
                categoriaRepository.save(new Categoria(null, "Artículos para bebés", "Pañales, ropa y artículos para bebés"));
                categoriaRepository.save(new Categoria(null, "Artículos escolares", "Útiles escolares y material educativo"));
                categoriaRepository.save(new Categoria(null, "Mobiliario", "Muebles y artículos para el hogar"));
                categoriaRepository.save(new Categoria(null, "Otros", "Otros artículos no categorizados"));
                System.out.println("✅ Categorías insertadas correctamente");
            }
        };
    }
}