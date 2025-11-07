package com.udea.fleetguard360F3.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CrearReservaDto(
        @NotNull Long viajeId,
        @NotNull Integer cantidadAsientos,
        List<PasajeroAdicionalDto> adicionales
) {}