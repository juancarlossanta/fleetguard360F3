package com.udea.fleetguard360F3.repository;

import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPasajeroId(Long pasajeroId);
    List<Reserva> findByViaje(Viaje viajeId);
}
