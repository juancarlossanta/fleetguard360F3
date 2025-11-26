package com.udea.fleetguard360F3.graphql;
import java.util.List;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.udea.fleetguard360F3.service.ReservaService;

@Controller
public class CiudadesController {

    private final ReservaService reservaService;

    public CiudadesController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @QueryMapping
    public List<String> buscarCiudades() {
        return reservaService.buscarCiudades();
    }        
}
