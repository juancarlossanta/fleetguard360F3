package com.udea.fleetguard360F3.dto;

public class ResponderEncuestaDto {

    private Long reservaId;
    private int calificacionPuntualidad;
    private int calificacionComodidad;
    private int calificacionAtencionConductor;
    private int calificacionPrestaciones;
    private int calificacionGeneral;
    private String comentarios;

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
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
}