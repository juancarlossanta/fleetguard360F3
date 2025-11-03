package com.udea.fleetguard360F3.graphql;

import com.udea.fleetguard360F3.dto.BuscarViajesDto;
import com.udea.fleetguard360F3.dto.CrearReservaDto;
import com.udea.fleetguard360F3.dto.PasajeroAdicionalDto;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.model.PasajeroAdicional;
import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.model.Viaje;
import com.udea.fleetguard360F3.service.PasajeroService;
import com.udea.fleetguard360F3.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReservaController {

    private final ReservaService reservaService;
    private final PasajeroService pasajeroService;

    public ReservaController(ReservaService reservaService, PasajeroService pasajeroService) {
        this.reservaService = reservaService;
        this.pasajeroService = pasajeroService;
    }


    @QueryMapping
    public List<Viaje> buscarViajes(@Argument @Valid BuscarViajesDto input) {
        return reservaService.buscarViajes(input.origen(), input.destino(), input.fecha());
    }

    @MutationMapping
    public ReservaResponse crearReserva(@Argument @Valid CrearReservaDto input, @Argument Long pasajeroId) {
        try {
            Pasajero pasajero = pasajeroService.findById(pasajeroId);
            if (pasajero == null) {
                return new ReservaResponse(false, "Pasajero no encontrado", null);
            }

            List<PasajeroAdicional> adicionales = null;
            if (input.adicionales() != null) {
                adicionales = input.adicionales().stream()
                        .map(dto -> {
                            PasajeroAdicional ad = new PasajeroAdicional();
                            ad.setNombre(dto.nombre());
                            ad.setDocumentoIdentidad(dto.identificacion());
                            return ad;
                        }).collect(Collectors.toList());
            }

            Reserva reserva = reservaService.crearReserva(
                    pasajero,
                    input.viajeId(),
                    adicionales,
                    input.cantidadAsientos()
            );

            return new ReservaResponse(true, "Reserva creada correctamente", reserva);

        } catch (IllegalArgumentException ex) {
            return new ReservaResponse(false, ex.getMessage(), null);
        }
    }

    @QueryMapping
    public List<Reserva> misReservas(@Argument Long pasajeroId) {
        return reservaService.reservasPorPasajero(pasajeroId);
    }

    @MutationMapping
    public ReservaResponse cancelarReserva(@Argument Long reservaId, @Argument Long pasajeroId) {
        try {
            Reserva reserva = reservaService.cancelarReserva(reservaId, pasajeroId);
            return new ReservaResponse(true, "Reserva cancelada exitosamente", reserva);
        } catch (IllegalArgumentException ex) {
            return new ReservaResponse(false, ex.getMessage(), null);
        }
    }

    public record ReservaResponse(boolean success, String message, Reserva reserva) {}
}
