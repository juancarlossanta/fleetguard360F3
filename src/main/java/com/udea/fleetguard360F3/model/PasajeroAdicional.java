package com.udea.fleetguard360F3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // **AÑADIR ESTE CONSTRUCTOR PARA TESTING**
    public PasajeroAdicional(String nombre, String documentoIdentidad) {
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
    }

}
