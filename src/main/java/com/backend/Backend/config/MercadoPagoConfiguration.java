package com.backend.Backend.config;

import com.mercadopago.MercadoPagoConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoConfiguration {

    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    @Value("${ACCESS_TOKEN}")
    public void configurarMercadoPago(String accessToken) {
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println("Mercado Pago configurado correctamente.");
    }
}