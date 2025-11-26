package com.udea.fleetguard360F3.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "encuesta_satisfaccion")
public class EncuestaSatisfaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false, unique = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", nullable = false)
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false)
    private Viaje viaje;

    @Column(nullable = false)
    private int calificacionPuntualidad; // 1-5

    @Column(nullable = false)
    private int calificacionComodidad; // 1-5

    @Column(nullable = false)
    private int calificacionAtencionConductor; // 1-5

    @Column(nullable = false)
    private int calificacionPrestaciones; // 1-5

    @Column(nullable = false)
    private int calificacionGeneral; // 1-5

    @Column(length = 1000)
    private String comentarios;

    @Column(nullable = false)
    private LocalDateTime fechaRespuesta;

    public EncuestaSatisfaccion() {
        this.fechaRespuesta = LocalDateTime.now();
    }

    public EncuestaSatisfaccion(Reserva reserva, Pasajero pasajero, Viaje viaje,
                                int calificacionPuntualidad, int calificacionComodidad,
                                int calificacionAtencionConductor, int calificacionPrestaciones,
                                int calificacionGeneral, String comentarios) {
        this.reserva = reserva;
        this.pasajero = pasajero;
        this.viaje = viaje;
        this.calificacionPuntualidad = calificacionPuntualidad;
        this.calificacionComodidad = calificacionComodidad;
        this.calificacionAtencionConductor = calificacionAtencionConductor;
        this.calificacionPrestaciones = calificacionPrestaciones;
        this.calificacionGeneral = calificacionGeneral;
        this.comentarios = comentarios;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public int getCalificacionPuntualidad() {
        return calificacionPuntualidad;
    }

    public void setCalificacionPuntualidad(int calificacionPuntualidad) {
        this.calificacionPuntualidad = calificacionPuntualidad;
    }

    public int getCalificacionComodidad() {
        return calificacionComodidad;
    }

    public void setCalificacionComodidad(int calificacionComodidad) {
        this.calificacionComodidad = calificacionComodidad;
    }

    public int getCalificacionAtencionConductor() {
        return calificacionAtencionConductor;
    }

    public void setCalificacionAtencionConductor(int calificacionAtencionConductor) {
        this.calificacionAtencionConductor = calificacionAtencionConductor;
    }

    public int getCalificacionPrestaciones() {
        return calificacionPrestaciones;
    }

    public void setCalificacionPrestaciones(int calificacionPrestaciones) {
        this.calificacionPrestaciones = calificacionPrestaciones;
    }

    public int getCalificacionGeneral() {
        return calificacionGeneral;
    }

    public void setCalificacionGeneral(int calificacionGeneral) {
        this.calificacionGeneral = calificacionGeneral;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}