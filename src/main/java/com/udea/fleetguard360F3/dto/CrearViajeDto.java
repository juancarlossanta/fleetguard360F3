package com.udea.fleetguard360F3.dto;

public record CrearViajeDto(
        String origen,
        String destino,
        String fecha,
        String horaSalida,
        String horaLlegada,
        int cuposTotales,
        String estado
) {}
