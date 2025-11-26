package com.udea.fleetguard360F3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BuscarViajesDto(
        @NotBlank String origen,
        @NotBlank String destino,
        @NotNull LocalDate fecha
) {}