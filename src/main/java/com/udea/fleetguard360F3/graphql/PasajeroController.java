package com.udea.fleetguard360F3.graphql;

import com.udea.fleetguard360F3.dto.RegistroPasajeroDto;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.service.PasajeroService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;

@Controller
public class PasajeroController {
    private final PasajeroService service;

    public PasajeroController(PasajeroService service) {
        this.service = service;
    }

    @MutationMapping
    public RegistroResponse registerPasajero(@Argument @Valid RegistroPasajeroDto input) {
        try {
            Pasajero p = service.register(input);
            return new RegistroResponse(true, "Registrado correctamente", p);
        } catch (IllegalArgumentException ex) {
            return new RegistroResponse(false, ex.getMessage(), null);
        } catch (Exception ex) {
            return new RegistroResponse(false, "Error interno", null);
        }
    }

    @QueryMapping
    public Boolean checkEmail(@Argument String email) {
        return service.emailAvailable(email);
    }

    @QueryMapping
    public Boolean checkUsername(@Argument String username) {
        return service.usernameAvailable(username);
    }

    @QueryMapping
    public Pasajero pasajeroById(@Argument Long id) {
        return service.findById(id);
    }

    static record RegistroResponse(boolean success, String message, Pasajero pasajero) {}
}
