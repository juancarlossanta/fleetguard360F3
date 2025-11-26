package com.udea.fleetguard360F3.graphql;

import com.udea.fleetguard360F3.model.PosicionBus;
import com.udea.fleetguard360F3.service.impl.BusPositionPublisherImpl;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

@Controller
public class BusSubscriptionController {

    private final BusPositionPublisherImpl publisher;

    public BusSubscriptionController(BusPositionPublisherImpl publisher) {
        this.publisher = publisher;
    }

    @SubscriptionMapping
    public Publisher<PosicionBus> busPositionUpdated(@Argument Long viajeId) {
        return publisher.getPublisher(viajeId);
    }
}
