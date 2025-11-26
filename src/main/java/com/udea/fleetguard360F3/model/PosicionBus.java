package com.udea.fleetguard360F3.model;

import java.time.LocalDateTime;

public class PosicionBus {
    private double latitud;
    private double longitud;
    private int etaMinutos;
    private LocalDateTime ultimaActualizacion;

    public PosicionBus(double latitud, double longitud, int etaMinutos, LocalDateTime ultimaActualizacion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.etaMinutos = etaMinutos;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public int getEtaMinutos() {
        return etaMinutos;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
}

