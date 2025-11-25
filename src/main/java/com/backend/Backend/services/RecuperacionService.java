package com.backend.Backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecuperacionService {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LONGITUD_CODIGO = 10;
    private static final long EXPIRACION_CODIGO_MS = 24 * 60 * 60 * 1000; // 24 horas

    public String generarCodigoRecuperacion() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        
        for (int i = 0; i < LONGITUD_CODIGO; i++) {
            codigo.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
        }
        
        return codigo.toString();
    }

    public boolean validarCodigoRecuperacion(String codigoAlmacenado, java.util.Date fechaCreacion) {
        if (codigoAlmacenado == null || codigoAlmacenado.isEmpty()) {
            return false;
        }
        
        if (fechaCreacion == null) {
            return false;
        }
        
        long ahora = System.currentTimeMillis();
        long tiempoTranscurrido = ahora - fechaCreacion.getTime();
        
        return tiempoTranscurrido <= EXPIRACION_CODIGO_MS;
    }
}
