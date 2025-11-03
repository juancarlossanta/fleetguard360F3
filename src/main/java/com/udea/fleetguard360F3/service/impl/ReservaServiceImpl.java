package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.model.*;
import com.udea.fleetguard360F3.repository.ReservaRepository;
import com.udea.fleetguard360F3.repository.ViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.udea.fleetguard360F3.service.ReservaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepo;
    private final ViajeRepository viajeRepo;

    public ReservaServiceImpl(ReservaRepository reservaRepo, ViajeRepository viajeRepo) {
        this.reservaRepo = reservaRepo;
        this.viajeRepo = viajeRepo;
    }

    @Override
    public List<Viaje> buscarViajes(String origen, String destino, LocalDate fecha) {
        return viajeRepo.findByOrigenAndDestinoAndFecha(origen, destino, fecha);
    }
    @Override
    @Transactional
    public Reserva crearReserva(Pasajero pasajero, Long viajeId, List<PasajeroAdicional> adicionales, int cantidadAsientos) {
        Viaje viaje = viajeRepo.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        if (viaje.getCuposDisponibles() < cantidadAsientos) {
            throw new IllegalArgumentException("Sin cupo disponible");
        }

        viaje.setCuposDisponibles(viaje.getCuposDisponibles() - cantidadAsientos);
        Reserva reserva = new Reserva();
        reserva.setPasajero(pasajero);
        reserva.setViaje(viaje);
        reserva.setCantidadAsientos(cantidadAsientos);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setCodigoReserva(UUID.randomUUID().toString());
        reserva.setEstado(Reserva.EstadoReserva.ACTIVA);

        if (adicionales != null) {
            adicionales.forEach(ad -> ad.setReserva(reserva));
            reserva.setPasajerosAdicionales(adicionales);
        }

        Reserva saved = reservaRepo.save(reserva);

        sendEmail(pasajero.getEmail(), "Reserva confirmada",
                "Tu reserva fue confirmada. Código: " + saved.getCodigoReserva());

        return saved;
    }
    @Override
    public List<Reserva> reservasPorPasajero(Long pasajeroId) {
        return reservaRepo.findByPasajeroId(pasajeroId);
    }

    @Override
    @Transactional
    public Reserva cancelarReserva(Long reservaId, Long pasajeroId) {
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        if (!reserva.getPasajero().getId().equals(pasajeroId)) {
            throw new IllegalArgumentException("No tienes permiso para cancelar esta reserva.");
        }

        if (reserva.getEstado() != Reserva.EstadoReserva.ACTIVA) {
            throw new IllegalArgumentException("Solo puedes cancelar reservas activas.");
        }

        Viaje viaje = reserva.getViaje();
        viaje.setCuposDisponibles(viaje.getCuposDisponibles() + reserva.getCantidadAsientos());

        reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
        reservaRepo.save(reserva);

        sendEmail(reserva.getPasajero().getEmail(), "Reserva cancelada",
                "Tu reserva con código " + reserva.getCodigoReserva() + " ha sido cancelada exitosamente.");

        return reserva;
    }

    public void sendEmail(String to, String subject, String body) {
        System.out.println("Enviando email a: " + to);
        System.out.println("Asunto: " + subject);
        System.out.println("Contenido: " + body);
    }
}
