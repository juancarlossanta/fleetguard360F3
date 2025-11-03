package com.udea.fleetguard360F3.graphql;

import com.udea.fleetguard360F3.model.Viaje;
import com.udea.fleetguard360F3.repository.ViajeRepository;
import com.udea.fleetguard360F3.dto.CrearViajeDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class ViajeController {

    private final ViajeRepository viajeRepo;

    public ViajeController(ViajeRepository viajeRepo) {
        this.viajeRepo = viajeRepo;
    }

    @MutationMapping
    public Viaje crearViaje(@Argument CrearViajeDto input) {
        Viaje viaje = new Viaje();
        viaje.setOrigen(input.origen());
        viaje.setDestino(input.destino());
        viaje.setFecha(LocalDate.parse(input.fecha()));
        viaje.setHoraSalida(LocalTime.parse(input.horaSalida()));
        viaje.setHoraLlegada(LocalTime.parse(input.horaLlegada()));
        viaje.setCuposTotales(input.cuposTotales());
        viaje.setCuposDisponibles(input.cuposTotales());
        viaje.setEstado(input.estado());
        return viajeRepo.save(viaje);
    }
}
