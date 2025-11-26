package com.udea.fleetguard360F3.service;

import com.udea.fleetguard360F3.dto.EstadisticasEncuestaDto;
import com.udea.fleetguard360F3.dto.ResponderEncuestaDto;
import com.udea.fleetguard360F3.model.EncuestaSatisfaccion;

import java.util.List;

public interface EncuestaSatisfaccionService {

    EncuestaSatisfaccion responderEncuesta(ResponderEncuestaDto dto, Long pasajeroId);

    boolean yaRespondioEncuesta(Long reservaId);

    List<EncuestaSatisfaccion> obtenerEncuestasPorPasajero(Long pasajeroId);

    List<EncuestaSatisfaccion> obtenerEncuestasPorViaje(Long viajeId);

    EstadisticasEncuestaDto obtenerEstadisticasGenerales();

    EstadisticasEncuestaDto obtenerEstadisticasPorViaje(Long viajeId);
}
