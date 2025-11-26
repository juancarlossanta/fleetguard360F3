package com.udea.fleetguard360F3.dto;

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

    public Long getId() {
        return id;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public String getDestino() {
        return destino;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public String getEstado() {
        return estado;
    }
}


