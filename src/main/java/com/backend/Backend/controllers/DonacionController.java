package com.backend.Backend.controllers;

import com.backend.Backend.dtos.donacion.DonacionRequest;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donaciones")
@CrossOrigin(origins = "*")
public class DonacionController {

    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    @PostMapping
    public Map<String, Object> crearPreferencia(@RequestBody DonacionRequest request) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);

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
