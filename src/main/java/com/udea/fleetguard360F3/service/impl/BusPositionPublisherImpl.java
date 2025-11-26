package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.model.PosicionBus;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BusPositionPublisherImpl {

    private final Map<Long, Sinks.Many<PosicionBus>> sinksPorViaje = new ConcurrentHashMap<>();

    public void publish(Long viajeId, PosicionBus posicion) {
        sinksPorViaje.computeIfAbsent(viajeId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .tryEmitNext(posicion);
    }

    public Publisher<PosicionBus> getPublisher(Long viajeId) {
        return sinksPorViaje
                .computeIfAbsent(viajeId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux();
    }
}
