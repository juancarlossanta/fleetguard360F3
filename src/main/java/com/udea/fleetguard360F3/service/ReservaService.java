package com.udea.fleetguard360F3.service;

import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.model.PasajeroAdicional;
import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.model.Viaje;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {
    List<Viaje> buscarViajes(String origen, String destino, LocalDate fecha);
    Reserva crearReserva(Pasajero pasajero, Long viajeId, List<PasajeroAdicional> adicionales, int cantidadAsientos);
    List<Reserva> reservasPorPasajero(Long pasajeroId);
    Reserva cancelarReserva(Long reservaId, Long pasajeroId);
}
