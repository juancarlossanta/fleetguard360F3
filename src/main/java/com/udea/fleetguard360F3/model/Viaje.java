package com.udea.fleetguard360F3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origen;
    private String destino;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private int cuposTotales;
    private int cuposDisponibles;
    private String estado;


    public Viaje() {
    }

    public Viaje(Long id, String origen, String destino, LocalDate fecha, LocalTime horaSalida, LocalTime horaLlegada,
                 int cuposTotales, int cuposDisponibles, String estado) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.cuposTotales = cuposTotales;
        this.cuposDisponibles = cuposDisponibles;
        this.estado = estado;
    }

}
