package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.dto.EstadisticasEncuestaDto;
import com.udea.fleetguard360F3.dto.ResponderEncuestaDto;
import com.udea.fleetguard360F3.model.EncuestaSatisfaccion;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.repository.EncuestaSatisfaccionRepository;
import com.udea.fleetguard360F3.repository.PasajeroRepository;
import com.udea.fleetguard360F3.repository.ReservaRepository;
import com.udea.fleetguard360F3.service.EncuestaSatisfaccionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EncuestaSatisfaccionServiceImpl implements EncuestaSatisfaccionService {

    private final EncuestaSatisfaccionRepository encuestaRepository;
    private final ReservaRepository reservaRepository;
    private final PasajeroRepository pasajeroRepository;

    public EncuestaSatisfaccionServiceImpl(
            EncuestaSatisfaccionRepository encuestaRepository,
            ReservaRepository reservaRepository,
            PasajeroRepository pasajeroRepository) {
        this.encuestaRepository = encuestaRepository;
        this.reservaRepository = reservaRepository;
        this.pasajeroRepository = pasajeroRepository;
    }

    @Override
    @Transactional
    public EncuestaSatisfaccion responderEncuesta(ResponderEncuestaDto dto, Long pasajeroId) {
        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        Pasajero pasajero = pasajeroRepository.findById(pasajeroId)
                .orElseThrow(() -> new IllegalArgumentException("Pasajero no encontrado"));

        if (!reserva.getPasajero().getId().equals(pasajeroId)) {
            throw new IllegalArgumentException("La reserva no pertenece a este pasajero");
        }

        if (reserva.getEstado() != Reserva.EstadoReserva.FINALIZADA) {
            throw new IllegalArgumentException("Solo se puede calificar viajes finalizados");
        }

        if (encuestaRepository.existsByReserva(reserva)) {
            throw new IllegalArgumentException("Ya has respondido la encuesta para este viaje");
        }

        validarCalificacion(dto.getCalificacionPuntualidad(), "puntualidad");
        validarCalificacion(dto.getCalificacionComodidad(), "comodidad");
        validarCalificacion(dto.getCalificacionAtencionConductor(), "atención del conductor");
        validarCalificacion(dto.getCalificacionPrestaciones(), "prestaciones");
        validarCalificacion(dto.getCalificacionGeneral(), "calificación general");

        EncuestaSatisfaccion encuesta = new EncuestaSatisfaccion(
                reserva,
                pasajero,
                reserva.getViaje(),
                dto.getCalificacionPuntualidad(),
                dto.getCalificacionComodidad(),
                dto.getCalificacionAtencionConductor(),
                dto.getCalificacionPrestaciones(),
                dto.getCalificacionGeneral(),
                dto.getComentarios()
        );

        EncuestaSatisfaccion guardada = encuestaRepository.save(encuesta);

        System.out.println(" Encuesta registrada para reserva #" + reserva.getId() +
                " - Calificación general: " + dto.getCalificacionGeneral());

        return guardada;
    }

    @Override
    public boolean yaRespondioEncuesta(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        return encuestaRepository.existsByReserva(reserva);
    }

    @Override
    public List<EncuestaSatisfaccion> obtenerEncuestasPorPasajero(Long pasajeroId) {
        return encuestaRepository.findByPasajeroId(pasajeroId);
    }

    @Override
    public List<EncuestaSatisfaccion> obtenerEncuestasPorViaje(Long viajeId) {
        return encuestaRepository.findByViaje(
                reservaRepository.findById(viajeId)
                        .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"))
                        .getViaje()
        );
    }

    @Override
    public EstadisticasEncuestaDto obtenerEstadisticasGenerales() {
        Double promedioPuntualidad = encuestaRepository.calcularPromedioPuntualidad();
        Double promedioComodidad = encuestaRepository.calcularPromedioComodidad();
        Double promedioAtencionConductor = encuestaRepository.calcularPromedioAtencionConductor();
        Double promedioPrestaciones = encuestaRepository.calcularPromedioPrestaciones();
        Double promedioGeneral = encuestaRepository.calcularPromedioGeneral();
        long total = encuestaRepository.count();

        return new EstadisticasEncuestaDto(
                promedioPuntualidad != null ? promedioPuntualidad : 0.0,
                promedioComodidad != null ? promedioComodidad : 0.0,
                promedioAtencionConductor != null ? promedioAtencionConductor : 0.0,
                promedioPrestaciones != null ? promedioPrestaciones : 0.0,
                promedioGeneral != null ? promedioGeneral : 0.0,
                total
        );
    }

    @Override
    public EstadisticasEncuestaDto obtenerEstadisticasPorViaje(Long viajeId) {
        List<EncuestaSatisfaccion> encuestas = obtenerEncuestasPorViaje(viajeId);

        if (encuestas.isEmpty()) {
            return new EstadisticasEncuestaDto(0, 0, 0, 0, 0, 0);
        }

        double promPunt = encuestas.stream().mapToInt(EncuestaSatisfaccion::getCalificacionPuntualidad).average().orElse(0);
        double promCom = encuestas.stream().mapToInt(EncuestaSatisfaccion::getCalificacionComodidad).average().orElse(0);
        double promAten = encuestas.stream().mapToInt(EncuestaSatisfaccion::getCalificacionAtencionConductor).average().orElse(0);
        double promPrest = encuestas.stream().mapToInt(EncuestaSatisfaccion::getCalificacionPrestaciones).average().orElse(0);
        double promGen = encuestas.stream().mapToInt(EncuestaSatisfaccion::getCalificacionGeneral).average().orElse(0);

        return new EstadisticasEncuestaDto(promPunt, promCom, promAten, promPrest, promGen, encuestas.size());
    }

    private void validarCalificacion(int calificacion, String aspecto) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException(
                    "La calificación de " + aspecto + " debe estar entre 1 y 5");
        }
    }
}