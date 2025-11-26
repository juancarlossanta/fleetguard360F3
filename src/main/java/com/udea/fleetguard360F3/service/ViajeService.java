package com.udea.fleetguard360F3.service;

import com.udea.fleetguard360F3.model.Viaje;

public interface ViajeService {
    Viaje actualizarEstado(Long viajeId, String estado);
}