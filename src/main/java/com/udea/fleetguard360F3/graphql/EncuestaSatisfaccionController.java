package com.udea.fleetguard360F3.graphql;

import com.udea.fleetguard360F3.dto.EstadisticasEncuestaDto;
import com.udea.fleetguard360F3.dto.ResponderEncuestaDto;
import com.udea.fleetguard360F3.model.EncuestaSatisfaccion;
import com.udea.fleetguard360F3.service.EncuestaSatisfaccionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import com.udea.fleetguard360F3.repository.PasajeroRepository;
import com.udea.fleetguard360F3.model.Pasajero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EncuestaSatisfaccionController {

    private final EncuestaSatisfaccionService encuestaService;
    private final PasajeroRepository pasajeroRepository;

    public EncuestaSatisfaccionController(EncuestaSatisfaccionService encuestaService, PasajeroRepository pasajeroRepository) {
        this.encuestaService = encuestaService;
        this.pasajeroRepository = pasajeroRepository;
    }

    @MutationMapping
    public Map<String, Object> responderEncuesta(@Argument ResponderEncuestaDto input) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long pasajeroId = obtenerPasajeroIdAutenticado();

            EncuestaSatisfaccion encuesta = encuestaService.responderEncuesta(input, pasajeroId);

            response.put("success", true);
            response.put("mensaje", "¡Gracias por tu opinión! Tu feedback nos ayuda a mejorar el servicio.");
            response.put("encuesta", encuesta);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("mensaje", e.getMessage());
            response.put("encuesta", null);
        }

        return response;
    }

    @QueryMapping
    public List<EncuestaSatisfaccion> misEncuestas() {
        Long pasajeroId = obtenerPasajeroIdAutenticado();
        return encuestaService.obtenerEncuestasPorPasajero(pasajeroId);
    }

    @QueryMapping
    public boolean yaRespondioEncuesta(@Argument Long reservaId) {
        return encuestaService.yaRespondioEncuesta(reservaId);
    }

    @QueryMapping
    public EstadisticasEncuestaDto estadisticasEncuestas() {
        return encuestaService.obtenerEstadisticasGenerales();
    }

    @QueryMapping
    public EstadisticasEncuestaDto estadisticasEncuestasPorViaje(@Argument Long viajeId) {
        return encuestaService.obtenerEstadisticasPorViaje(viajeId);
    }

    @QueryMapping
    public List<EncuestaSatisfaccion> encuestasPorViaje(@Argument Long viajeId) {
        return encuestaService.obtenerEncuestasPorViaje(viajeId);
    }

    /**
     * Obtiene el ID del pasajero autenticado desde el JWT
     * @return ID del pasajero autenticado
     * @throws IllegalArgumentException si no está autenticado o no se encuentra el pasajero
     */
    private Long obtenerPasajeroIdAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("DEBUG - Authentication: " + auth);
        System.out.println("DEBUG - Is Authenticated: " + (auth != null ? auth.isAuthenticated() : "null"));
        System.out.println("DEBUG - Principal: " + (auth != null ? auth.getPrincipal() : "null"));
        System.out.println("DEBUG - Name: " + (auth != null ? auth.getName() : "null"));

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        // Validar que existe autenticación
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        // Validar que no sea usuario anónimo
        if ("anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        // Obtener el username del JWT
        String username = auth.getName();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username no encontrado en el token");
        }

        // Buscar el pasajero
        Pasajero pasajero = pasajeroRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un pasajero con el username: " + username));

        System.out.println(" Pasajero autenticado: " + username + " (ID: " + pasajero.getId() + ")");

        return pasajero.getId();
    }
}