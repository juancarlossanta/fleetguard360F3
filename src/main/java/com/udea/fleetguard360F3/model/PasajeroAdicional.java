package com.udea.fleetguard360F3.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pasajero_adicional")

public class PasajeroAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String documentoIdentidad;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    public PasajeroAdicional() {
    }

    public PasajeroAdicional(Long id, String nombre, String documentoIdentidad, Reserva reserva) {
        this.id = id;
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
        this.reserva = reserva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
}
