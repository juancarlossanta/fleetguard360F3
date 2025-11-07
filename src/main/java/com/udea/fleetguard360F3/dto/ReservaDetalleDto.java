package com.udea.fleetguard360F3.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDetalleDto {
    private Long id;
    private String codigoReserva;
    private String destino;
    private String fechaViaje;
    private String estado;

    public ReservaDetalleDto(Long id, String codigoReserva, String destino, String fechaViaje, String estado) {
        this.id = id;
        this.codigoReserva = codigoReserva;
        this.destino = destino;
        this.fechaViaje = fechaViaje;
        this.estado = estado;
    }

}


