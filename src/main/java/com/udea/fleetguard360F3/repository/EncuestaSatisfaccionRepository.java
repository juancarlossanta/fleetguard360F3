package com.udea.fleetguard360F3.repository;

import com.udea.fleetguard360F3.model.EncuestaSatisfaccion;
import com.udea.fleetguard360F3.model.Reserva;
import com.udea.fleetguard360F3.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EncuestaSatisfaccionRepository extends JpaRepository<EncuestaSatisfaccion, Long> {

    boolean existsByReserva(Reserva reserva);

    Optional<EncuestaSatisfaccion> findByReserva(Reserva reserva);

    List<EncuestaSatisfaccion> findByViaje(Viaje viaje);

    List<EncuestaSatisfaccion> findByPasajeroId(Long pasajeroId);

    @Query("SELECT AVG(e.calificacionPuntualidad) FROM EncuestaSatisfaccion e")
    Double calcularPromedioPuntualidad();

    @Query("SELECT AVG(e.calificacionComodidad) FROM EncuestaSatisfaccion e")
    Double calcularPromedioComodidad();

    @Query("SELECT AVG(e.calificacionAtencionConductor) FROM EncuestaSatisfaccion e")
    Double calcularPromedioAtencionConductor();

    @Query("SELECT AVG(e.calificacionPrestaciones) FROM EncuestaSatisfaccion e")
    Double calcularPromedioPrestaciones();

    @Query("SELECT AVG(e.calificacionGeneral) FROM EncuestaSatisfaccion e")
    Double calcularPromedioGeneral();

    @Query("SELECT AVG(e.calificacionGeneral) FROM EncuestaSatisfaccion e WHERE e.viaje.id = :viajeId")
    Double calcularPromedioGeneralPorViaje(Long viajeId);
}
