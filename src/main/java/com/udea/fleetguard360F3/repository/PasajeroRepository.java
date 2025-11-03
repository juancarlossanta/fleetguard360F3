package com.udea.fleetguard360F3.repository;

import com.udea.fleetguard360F3.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    Optional<Pasajero> findByEmail(String email);
    Optional<Pasajero> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
