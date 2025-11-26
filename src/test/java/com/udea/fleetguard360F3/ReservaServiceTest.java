package com.udea.fleetguard360F3;


import com.udea.fleetguard360F3.model.*;
import com.udea.fleetguard360F3.repository.ReservaRepository;
import com.udea.fleetguard360F3.repository.ViajeRepository;
import com.udea.fleetguard360F3.service.impl.ReservaServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    // 1. DEPENDENCIAS (Mocks)
    @Mock
    private ReservaRepository reservaRepo;

    @Mock
    private ViajeRepository viajeRepo;

    // 2. CLASE A PROBAR (Spy/InjectMocks)
    @Spy
    @InjectMocks
    private ReservaServiceImpl reservaService;

    // Entidades de prueba
    private Pasajero pasajero;
    private Viaje viajeConCupo;
    private Viaje viajeAgotado;
    private Reserva reservaActiva;

    @BeforeEach
    void setUp() {
        pasajero = new Pasajero();
        pasajero.setId(10L);
        pasajero.setEmail("pasajero@test.com");

        // Viaje con cupo
        viajeConCupo = new Viaje();
        viajeConCupo.setId(100L);
        viajeConCupo.setOrigen("Medellin");
        viajeConCupo.setDestino("Bogota");
        viajeConCupo.setCuposDisponibles(10);

        // Reserva Activa
        reservaActiva = new Reserva();
        reservaActiva.setId(1L);
        reservaActiva.setPasajero(pasajero);
        reservaActiva.setViaje(viajeConCupo);
        reservaActiva.setCantidadAsientos(2);
        reservaActiva.setEstado(Reserva.EstadoReserva.ACTIVA);

        // Viaje agotado para excepción
        viajeAgotado = new Viaje();
        viajeAgotado.setId(200L);
        viajeAgotado.setCuposDisponibles(0);
    }

    // =====================================================================================
    // TEST PARA BÚSQUEDA (CP-HU02-01)
    // =====================================================================================

    @Test
    void testBuscarViajes_CPHU0201_BusquedaExitosa() {
        // A (Arrange): Preparación
        String origen = "Medellin";
        String destino = "Bogota";
        LocalDate fecha = LocalDate.now();
        List<Viaje> viajesSimulados = Collections.singletonList(viajeConCupo);

        when(viajeRepo.findByOrigenAndDestinoAndFecha(origen, destino, fecha))
                .thenReturn(viajesSimulados);

        // A (Act): Ejecución
        List<Viaje> resultado = reservaService.buscarViajes(origen, destino, fecha);

        // A (Assert): Verificación
        assertNotNull(resultado, "Debe retornar una lista, no nulo.");
        assertEquals(1, resultado.size(), "Debe retornar el viaje simulado.");
        verify(viajeRepo, times(1)).findByOrigenAndDestinoAndFecha(origen, destino, fecha);
    }

    // =====================================================================================
    // TEST PARA CREAR RESERVA (CP-HU02-03)
    // =====================================================================================

    @Test
    void testCrearReserva_CPHU0203_CaminoFeliz() {
        // A (Arrange): Preparación
        int asientosDeseados = 3;
        int cupoInicial = viajeConCupo.getCuposDisponibles(); // 10

        // Simular pasajeros adicionales (CP-HU02-04)
        PasajeroAdicional adicional1 = new PasajeroAdicional(100001, "Leo", "123", new Reserva());
        List<PasajeroAdicional> adicionales = Collections.singletonList(adicional1);

        when(viajeRepo.findById(viajeConCupo.getId())).thenReturn(Optional.of(viajeConCupo));
        // Mockear el guardado para retornar la reserva con un código de prueba
        when(reservaRepo.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setCodigoReserva("COD123");
            return r;
        });

        // Mockear el método privado sendEmail (usando Spy)
        // doNothing().when(reservaService).sendEmail(anyString(), anyString(), anyString());

        // A (Act): Ejecución
        Reserva resultado = reservaService.crearReserva(pasajero, viajeConCupo.getId(), adicionales, asientosDeseados);

        // A (Assert): Verificación
        assertNotNull(resultado, "La reserva debe ser creada.");
        assertEquals(Reserva.EstadoReserva.ACTIVA, resultado.getEstado());
        assertEquals(cupoInicial - asientosDeseados, viajeConCupo.getCuposDisponibles(), "Los cupos deben descontarse (10 - 3 = 7).");
        assertEquals(1, resultado.getPasajerosAdicionales().size(), "Debe incluir el pasajero adicional.");

        // Verificar interacciones
        verify(viajeRepo, times(1)).findById(viajeConCupo.getId());
        verify(reservaRepo, times(1)).save(any(Reserva.class));
        // verify(reservaService, times(1)).sendEmail(eq(pasajero.getEmail()), anyString(), contains("COD123"));
    }

    @Test
    void testCrearReserva_CPHU0203_SinCupoDisponible() {
        // A (Arrange): Preparación
        int asientosDeseados = 1; // Aunque es 1, el viaje agotado tiene 0 cupos

        when(viajeRepo.findById(viajeAgotado.getId())).thenReturn(Optional.of(viajeAgotado));

        // A (Act & Assert): Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(pasajero, viajeAgotado.getId(), null, asientosDeseados);
        });

        assertEquals("Sin cupo disponible", exception.getMessage());

        // Verifica que no se intentó guardar la reserva
        verify(reservaRepo, never()).save(any(Reserva.class));
    }

    // =====================================================================================
    // TEST PARA CANCELAR RESERVA
    // =====================================================================================

    @Test
    void testCancelarReserva_CaminoFeliz() {
        // A (Arrange): Preparación
        int cupoInicialViaje = viajeConCupo.getCuposDisponibles(); // 10
        int asientosReserva = reservaActiva.getCantidadAsientos(); // 2

        when(reservaRepo.findById(reservaActiva.getId())).thenReturn(Optional.of(reservaActiva));
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaActiva);
        // doNothing().when(reservaService).sendEmail(anyString(), anyString(), anyString());

        // A (Act): Ejecución
        Reserva resultado = reservaService.cancelarReserva(reservaActiva.getId(), pasajero.getId());

        // A (Assert): Verificación
        assertEquals(Reserva.EstadoReserva.CANCELADA, resultado.getEstado(), "El estado debe ser CANCELADA.");
        assertEquals(cupoInicialViaje + asientosReserva, viajeConCupo.getCuposDisponibles(), "Los cupos deben retornar al viaje (10 + 2 = 12).");

        // Verificar interacciones
        verify(reservaRepo, times(1)).save(reservaActiva);
        // verify(reservaService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testCancelarReserva_PermisoDenegado() {
        // A (Arrange): Preparación
        Long otroPasajeroId = 99L;

        when(reservaRepo.findById(reservaActiva.getId())).thenReturn(Optional.of(reservaActiva));

        // A (Act & Assert): Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.cancelarReserva(reservaActiva.getId(), otroPasajeroId);
        });

        assertEquals("No tienes permiso para cancelar esta reserva.", exception.getMessage());
        verify(reservaRepo, never()).save(any(Reserva.class));
    }

    @Test
    void testCancelarReserva_ReservaYaCancelada() {
        // A (Arrange): Preparación
        reservaActiva.setEstado(Reserva.EstadoReserva.CANCELADA);

        when(reservaRepo.findById(reservaActiva.getId())).thenReturn(Optional.of(reservaActiva));

        // A (Act & Assert): Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.cancelarReserva(reservaActiva.getId(), pasajero.getId());
        });

        assertEquals("Solo puedes cancelar reservas activas.", exception.getMessage());
        verify(reservaRepo, never()).save(any(Reserva.class));
    }

}
