package com.udea.fleetguard360F3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", nullable = false)
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false)
    private Viaje viaje;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasajeroAdicional> pasajerosAdicionales;

    private int cantidadAsientos;

    private LocalDateTime fechaReserva;

    private String codigoReserva;

    //private String estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado = EstadoReserva.ACTIVA;


    public Reserva() {
    }

    public Reserva(Long id, Pasajero pasajero, Viaje viaje, List<PasajeroAdicional> pasajerosAdicionales,
                   int cantidadAsientos, LocalDateTime fechaReserva, String codigoReserva, EstadoReserva estado) {
        this.id = id;
        this.pasajero = pasajero;
        this.viaje = viaje;
        this.pasajerosAdicionales = pasajerosAdicionales;
        this.cantidadAsientos = cantidadAsientos;
        this.fechaReserva = fechaReserva;
        this.codigoReserva = codigoReserva;
        this.estado = estado != null ? estado : EstadoReserva.ACTIVA;
    }

    public enum EstadoReserva {
        ACTIVA,
        CANCELADA,
        FINALIZADA
    }
}

