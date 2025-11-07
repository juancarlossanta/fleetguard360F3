package com.udea.fleetguard360F3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservaDetalleDto {
    private Long id;
    private String codigoReserva;
    private String destino;
    private String fechaViaje;
    private String estado;

}


