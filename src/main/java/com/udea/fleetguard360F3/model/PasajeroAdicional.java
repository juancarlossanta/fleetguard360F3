package com.udea.fleetguard360F3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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

    // **AÑADIR ESTE CONSTRUCTOR PARA TESTING**
    public PasajeroAdicional(String nombre, String documentoIdentidad) {
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
    }

}
