package com.udea.fleetguard360F3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.udea.fleetguard360F3.model.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    @Query("SELECT c.nombre FROM Ciudad c")
    List<String> findAllNombre();
}
