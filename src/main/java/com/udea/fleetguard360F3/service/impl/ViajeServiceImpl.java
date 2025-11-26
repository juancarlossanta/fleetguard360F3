package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.model.Viaje;
import com.udea.fleetguard360F3.repository.ViajeRepository;
import com.udea.fleetguard360F3.service.ViajeService;
import org.springframework.stereotype.Service;

@Service
public class ViajeServiceImpl implements ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeServiceImpl(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    @Override
    public Viaje actualizarEstado(Long viajeId, String estado) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        viaje.setEstado(estado);
        return viajeRepository.save(viaje);
    }
}