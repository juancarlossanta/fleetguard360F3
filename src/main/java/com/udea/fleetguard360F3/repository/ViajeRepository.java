package com.udea.fleetguard360F3.repository;

import com.udea.fleetguard360F3.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByOrigenAndDestinoAndFecha(String origen, String destino, LocalDate fecha);
    List<Viaje> findByEstado(String estado);
}
