package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.model.PosicionBus;
import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.model.Viaje;
import com.udea.fleetguard360F3.repository.ReservaRepository;
import com.udea.fleetguard360F3.repository.ViajeRepository;
import com.udea.fleetguard360F3.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class BusTrackerSimulatorImpl {

    private final BusPositionPublisherImpl publisher;
    private final ViajeRepository viajeRepository;
    private final ReservaRepository reservaRepository;
    private final EmailService emailService;
    private final Random random = new Random();

    public BusTrackerSimulatorImpl(
            BusPositionPublisherImpl publisher,
            ViajeRepository viajeRepository,
            ReservaRepository reservaRepository,
            EmailService emailService
    ) {
        this.publisher = publisher;
        this.viajeRepository = viajeRepository;
        this.reservaRepository = reservaRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 5000) // cada 5 segundos
    public void actualizarPosicion() {
        System.out.println("Ejecutando actualización de posición...");

        // Obtener los viajes activos
        List<Viaje> viajesActivos = viajeRepository.findByEstado("ACTIVO");

        System.out.println("Viajes activos encontrados: " + viajesActivos.size());

        for (Viaje viaje : viajesActivos) {
            Long viajeId = viaje.getId();

            // posición simulada
            double lat = 6.15 + random.nextDouble() * 0.01;
            double lon = -75.37 + random.nextDouble() * 0.01;
            //int eta = 5 + random.nextInt(5); // minutos estimados
            int eta = random.nextInt(6);

            PosicionBus posicion = new PosicionBus(lat, lon, eta, LocalDateTime.now());

            publisher.publish(viajeId, posicion);

            List<Reserva> reservas = reservaRepository.findByViaje(viaje);

            //  Enviar correo a los pasajeros
            for (Reserva reserva : reservas) {
                if (eta <= 2 && !reserva.isAlertaEnviada() && reserva.getEstado() == Reserva.EstadoReserva.ACTIVA && reserva.getPasajero() != null && reserva.getPasajero().getEmail() != null) {
                    String correo = reserva.getPasajero().getEmail();
                    String asunto = " Tu bus está por llegar";
                    String cuerpo = "Hola " + reserva.getPasajero().getNombre() +
                            ", tu bus del viaje #" + viajeId + " llegará en aproximadamente " + eta + " minutos.";
                    System.out.println("Enviando correo a " + correo + " para el viaje " + viajeId);

                    try {
                        emailService.sendEmailWithRetry(correo, asunto, cuerpo);

                        // Marcar como enviada
                        reserva.setAlertaEnviada(true);
                        reserva.setFechaAlerta(LocalDateTime.now());
                        reservaRepository.save(reserva);

                        System.out.println(" Alerta enviada y registrada para reserva #" + reserva.getId());
                    } catch (Exception e) {
                        System.err.println(" Error al enviar alerta: " + e.getMessage());
                    }
                }
            }

            System.out.println(" Posición actualizada para viaje " + viajeId + ": " + posicion);
        }
    }
}

