package com.backend.Backend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase utilitaria para cálculos geoespaciales y búsquedas por radio de distancia.
 * Utiliza la fórmula de Haversine para calcular distancias entre coordenadas geográficas.
 */
public class GeoLocationUtils {

    // Radio de la Tierra en kilómetros
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calcula un bounding box (rectángulo de búsqueda) alrededor de un punto central
     * basado en una distancia en kilómetros.
     *
     * @param latitud la latitud del punto central
     * @param longitud la longitud del punto central
     * @param distanciaKM la distancia en kilómetros
     * @return un objeto BoundingBox con los límites mínimos y máximos de latitud y longitud
     */
    public static BoundingBox calcularBoundingBox(double latitud, double longitud, double distanciaKM) {
        // Convertir latitud y longitud a radianes
        double latRad = Math.toRadians(latitud);

        // Calcular el cambio de latitud en grados
        double deltaLatitud = Math.toDegrees(distanciaKM / EARTH_RADIUS_KM);

        // Calcular el cambio de longitud en grados (varia según la latitud)
        double deltaLongitud = Math.toDegrees(
            Math.asin(Math.sin(distanciaKM / EARTH_RADIUS_KM) / Math.cos(latRad))
        );

        // Calcular los límites
        double latMin = latitud - deltaLatitud;
        double latMax = latitud + deltaLatitud;
        double lonMin = longitud - deltaLongitud;
        double lonMax = longitud + deltaLongitud;

        return new BoundingBox(latMin, latMax, lonMin, lonMax);
    }

    /**
     * Calcula la distancia en kilómetros entre dos puntos geográficos
     * utilizando la fórmula de Haversine.
     *
     * @param lat1 latitud del primer punto
     * @param lon1 longitud del primer punto
     * @param lat2 latitud del segundo punto
     * @param lon2 longitud del segundo punto
     * @return la distancia en kilómetros
     */
    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Valida que las coordenadas sean válidas.
     *
     * @param latitud la latitud a validar (debe estar entre -90 y 90)
     * @param longitud la longitud a validar (debe estar entre -180 y 180)
     * @return true si las coordenadas son válidas, false en caso contrario
     */
    public static boolean sonCoordenadasValidas(double latitud, double longitud) {
        return latitud >= -90 && latitud <= 90 && longitud >= -180 && longitud <= 180;
    }

    /**
     * Clase que representa un rectángulo de búsqueda geográfica (bounding box).
     * Contiene los límites mínimos y máximos de latitud y longitud.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundingBox {
        private double latMin;
        private double latMax;
        private double lonMin;
        private double lonMax;

        /**
         * Verifica si un punto geográfico se encuentra dentro del bounding box.
         *
         * @param latitud la latitud del punto a verificar
         * @param longitud la longitud del punto a verificar
         * @return true si el punto está dentro del bounding box, false en caso contrario
         */
        public boolean contienePunto(double latitud, double longitud) {
            return latitud >= latMin && latitud <= latMax &&
                   longitud >= lonMin && longitud <= lonMax;
        }
    }
}

