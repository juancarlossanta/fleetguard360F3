package com.udea.fleetguard360F3.dto;

public class EstadisticasEncuestaDto {

    private double promedioPuntualidad;
    private double promedioComodidad;
    private double promedioAtencionConductor;
    private double promedioPrestaciones;
    private double promedioGeneral;
    private long totalEncuestas;

    public EstadisticasEncuestaDto(double promedioPuntualidad, double promedioComodidad,
                                   double promedioAtencionConductor, double promedioPrestaciones,
                                   double promedioGeneral, long totalEncuestas) {
        this.promedioPuntualidad = promedioPuntualidad;
        this.promedioComodidad = promedioComodidad;
        this.promedioAtencionConductor = promedioAtencionConductor;
        this.promedioPrestaciones = promedioPrestaciones;
        this.promedioGeneral = promedioGeneral;
        this.totalEncuestas = totalEncuestas;
    }

    public double getPromedioPuntualidad() {
        return promedioPuntualidad;
    }

    public void setPromedioPuntualidad(double promedioPuntualidad) {
        this.promedioPuntualidad = promedioPuntualidad;
    }

    public double getPromedioComodidad() {
        return promedioComodidad;
    }

    public void setPromedioComodidad(double promedioComodidad) {
        this.promedioComodidad = promedioComodidad;
    }

    public double getPromedioAtencionConductor() {
        return promedioAtencionConductor;
    }

    public void setPromedioAtencionConductor(double promedioAtencionConductor) {
        this.promedioAtencionConductor = promedioAtencionConductor;
    }

    public double getPromedioPrestaciones() {
        return promedioPrestaciones;
    }

    public void setPromedioPrestaciones(double promedioPrestaciones) {
        this.promedioPrestaciones = promedioPrestaciones;
    }

    public double getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(double promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    public long getTotalEncuestas() {
        return totalEncuestas;
    }

    public void setTotalEncuestas(long totalEncuestas) {
        this.totalEncuestas = totalEncuestas;
    }
}
