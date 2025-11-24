package com.backend.Backend.controllers;

import com.backend.Backend.dtos.donacion.DonacionRequest;
import com.backend.Backend.models.Proyecto;
import com.backend.Backend.models.Usuario;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import com.backend.Backend.services.ProyectoService;
import com.backend.Backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/donaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DonacionController {

    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    private final ProyectoService proyectoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public Map<String, Object> crearPreferencia(@RequestBody DonacionRequest request) {
        try {
            if (request.getNombreProyecto() == null || request.getNombreProyecto().trim().isEmpty()) {
                return Map.of("error", "Debe especificar 'nombreProyecto' en la petición para identificar el proyecto.");
            }

            List<Proyecto> encontrados = proyectoService.getProyectos(null, null, request.getNombreProyecto());
            if (encontrados.isEmpty()) {
                return Map.of("error", "Proyecto no encontrado con nombre: " + request.getNombreProyecto());
            }
            Proyecto proyecto = encontrados.stream()
                    .filter(p -> p.getNombre() != null && p.getNombre().equalsIgnoreCase(request.getNombreProyecto()))
                    .findFirst()
                    .orElse(encontrados.get(0));
            if (proyecto.getOrganizacion() == null) {
                return Map.of("error", "El proyecto no tiene una organización asociada.");
            }

            Optional<Usuario> orgOpt = usuarioService.getUsuarioById(proyecto.getOrganizacion().getId());
            if (orgOpt.isEmpty()) {
                return Map.of("error", "Organización asociada al proyecto no encontrada.");
            }

            String orgToken = orgOpt.get().getMercadoPagoAccessToken();
            if (orgToken == null || orgToken.trim().isEmpty()) {
                return Map.of("error", "La organización no tiene token de Mercado Pago configurado.");
            }

            MercadoPagoConfig.setAccessToken(orgToken);

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Donación a " + request.getNombreProyecto())
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(request.getMonto()))
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://circulo-solidario.github.io/Frontend/principal/detalles-proyecto")
                    .failure("https://circulo-solidario.github.io/Frontend/principal/detalles-proyecto")
                    .pending("https://circulo-solidario.github.io/Frontend/principal/detalles-proyecto")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return Map.of("preferenceId", preference.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }
}
