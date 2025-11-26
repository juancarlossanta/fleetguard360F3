package com.udea.fleetguard360F3.graphql;

import com.udea.fleetguard360F3.dto.LoginPasajeroDto;
import com.udea.fleetguard360F3.dto.ResetPasswordDto;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LoginPasajeroController {
    private final PasajeroService service;

    public LoginPasajeroController(PasajeroService service) {
        this.service = service;
    }


    @MutationMapping
    public LoginResponse login(@Argument @Valid LoginPasajeroDto input) {
        try {
            Pasajero p = service.authenticate(input.username(), input.password());
            String token = service.generateToken(p);
            return new LoginResponse(true, "Login exitoso", token,p);
        } catch (IllegalArgumentException ex) {
            return new LoginResponse(false, ex.getMessage(), null,null);
        }
    }

    @MutationMapping
    public boolean sendPasswordReset(@Argument String email) {
        return service.sendPasswordReset(email);
    }



    @MutationMapping
    public ResetPasswordResponse resetPassword(@Argument @Valid ResetPasswordDto input) {
        try {
            service.resetPassword(input.token(), input.newPassword());
            return new ResetPasswordResponse(true, "Contraseña restablecida correctamente");
        } catch (IllegalArgumentException ex) {
            return new ResetPasswordResponse(false, ex.getMessage());
        }
    }


    static record LoginResponse(boolean success, String message, String token, Pasajero pasajero) {}
    static record ResetPasswordResponse(boolean success, String message) {}
}
