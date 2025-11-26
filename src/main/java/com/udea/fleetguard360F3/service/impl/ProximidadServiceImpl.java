package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProximidadServiceImpl {

    private final EmailService emailService;

    private final Set<Long> reservasNotificadas = new HashSet<>();

    public ProximidadServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Simula la detección de proximidad (cuando el bus entra por primera vez a la geocerca)
     */
    public void procesarProximidad(Reserva reserva, String busId, int etaMinutos) {
        var pasajero = reserva.getPasajero();

        if (pasajero == null) {
            System.err.println(" Reserva sin pasajero asociado");
            return;
        }

        String subject = "¡Tu bus está cerca!";
        String body = "El bus " + busId + " llegará en aproximadamente " + etaMinutos + " minutos.";

        emailService.sendEmailWithRetry(
                pasajero.getEmail(),
                subject,
                body
        );

        System.out.println(" Correo enviado a " + pasajero.getEmail());
    }


};