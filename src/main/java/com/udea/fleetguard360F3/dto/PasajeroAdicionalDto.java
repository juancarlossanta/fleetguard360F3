package com.udea.fleetguard360F3.dto;

import jakarta.validation.constraints.NotBlank;

public record PasajeroAdicionalDto(
        @NotBlank String nombre,
        @NotBlank String identificacion
) {}