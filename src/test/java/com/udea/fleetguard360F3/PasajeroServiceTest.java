package com.udea.fleetguard360F3;


import com.udea.fleetguard360F3.dto.RegistroPasajeroDto;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.repository.PasajeroRepository;

import com.udea.fleetguard360F3.service.impl.PasajeroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasajeroServiceTest {

    // 1. DEPENDENCIAS (Mocks)
    @Mock
    private PasajeroRepository repo;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    // Simulación de la clave JWT
    private final Key mockJwtKey = new SecretKeySpec("EstaEsUnaClaveSecretaDe32BytesParaJWT".getBytes(), "HmacSHA256");

    // 2. CLASE A PROBAR (Spy/InjectMocks)
    // CORRECCIÓN: Quitamos la instanciación manual. Dejamos que Mockito inyecte
    // las dependencias (repo, passwordEncoder, mockJwtKey) en el constructor del servicio.
    @Spy
    @InjectMocks
    private PasajeroServiceImpl pasajeroService;

    private RegistroPasajeroDto inputValido;
    private Pasajero pasajeroExistente;
    private final String HASHED_PASS = "HASHED_PASSWORD_MOCK";

    @BeforeEach
    void setUp() {
        // CORRECCIÓN: Usar constructor por defecto y setters para RegistroPasajeroDto
        inputValido = new RegistroPasajeroDto();
        inputValido.setUsername("juan.quintero");
        inputValido.setNombre("Juan");
        inputValido.setApellido("Quintero");
        inputValido.setIdentificacion("12345678");
        inputValido.setTelefono("+573001234567");
        inputValido.setEmail("juan.quintero@ejemplo.com");
        inputValido.setPassword("Pasajero2025");
        inputValido.setPasswordConfirm("Pasajero2025");

        pasajeroExistente = new Pasajero();
        pasajeroExistente.setUsername(inputValido.getUsername());
        pasajeroExistente.setEmail(inputValido.getEmail());
        pasajeroExistente.setPasswordHash(HASHED_PASS);
    }

    // =====================================================================================
    // TEST PARA REGISTRO (CP-HU01)
    // =====================================================================================

    @Test
    void testRegister_CPHU0101_CaminoFeliz() {
        // A (Arrange): Preparación
        Pasajero pasajeroSimulado = new Pasajero();
        pasajeroSimulado.setId(1L);

        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(HASHED_PASS);
        when(repo.save(any(Pasajero.class))).thenReturn(pasajeroSimulado);

        // A (Act): Ejecución
        Pasajero resultado = pasajeroService.register(inputValido);

        // A (Assert): Verificación
        assertNotNull(resultado, "El pasajero debe ser retornado.");
        assertEquals(1L, resultado.getId());

        // Verifica que las interacciones críticas se dieron
        verify(repo, times(1)).save(any(Pasajero.class));
        verify(passwordEncoder, times(1)).encode(inputValido.getPassword());
    }

    @Test
    void testRegister_CPHU0103_ContrasenasNoCoinciden() {
        // A (Arrange): Preparación
        inputValido.setPasswordConfirm("NoCoincide2025"); // Cambia el input

        // A (Act & Assert): Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pasajeroService.register(inputValido);
        });

        assertEquals("Las contraseñas no coinciden.", exception.getMessage());

        // Verifica que no se llamó al repositorio (por lo que el método es eficiente)
        verify(repo, never()).existsByEmail(anyString());
    }

    @Test
    void testRegister_CPHU0102_EmailYaRegistrado() {
        // A (Arrange): Preparación
        when(repo.existsByEmail(inputValido.getEmail())).thenReturn(true);

        // A (Act & Assert): Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pasajeroService.register(inputValido);
        });

        assertEquals("El email ya está registrado.", exception.getMessage());

        // Verifica que la verificación de username no se ejecutó, ya que falló en email
        verify(repo, times(1)).existsByEmail(anyString());
        verify(repo, never()).existsByUsername(anyString());
    }

    // =====================================================================================
    // TEST PARA AUTENTICACIÓN (CP-HU04-01)
    // =====================================================================================

    @Test
    void testAuthenticate_CPHU0401_LoginExitoso() {
        // A (Arrange): Preparación
        when(repo.findByUsername(inputValido.getUsername())).thenReturn(Optional.of(pasajeroExistente));
        when(passwordEncoder.matches(inputValido.getPassword(), HASHED_PASS)).thenReturn(true);

        // A (Act): Ejecución
        Pasajero resultado = pasajeroService.authenticate(inputValido.getUsername(), inputValido.getPassword());

        // A (Assert): Verificación
        assertNotNull(resultado, "Debe retornar el objeto Pasajero.");
        verify(passwordEncoder, times(1)).matches(inputValido.getPassword(), HASHED_PASS);
    }

    @Test
    void testAuthenticate_UsuarioNoEncontrado() {
        // A (Arrange): Preparación
        when(repo.findByUsername(anyString())).thenReturn(Optional.empty());

        // A (Act & Assert): Ejecución y Verificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pasajeroService.authenticate("usuario_inexistente", "password");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    // =====================================================================================
    // TEST PARA RESETEO DE CONTRASEÑA (CP-HU04-02 y CPHU04-03)
    // =====================================================================================

    @Test
    void testSendPasswordReset_CPHU0402_EnvioExitoso() {
        // A (Arrange): Preparación
        // Mockear el método privado sendEmail para evitar la salida de consola y el Timer
        doNothing().when(pasajeroService).sendEmail(anyString(), anyString(), anyString());

        // Evitar que el Timer se ejecute en el test. Necesitas mockear la clase Timer o la tarea.
        // Dado que Timer es una dependencia de Java, la simulación es compleja.
        // Simplificamos verificando la lógica que no depende de tiempo.

        when(repo.findByEmail(inputValido.getEmail())).thenReturn(Optional.of(pasajeroExistente));

        // A (Act): Ejecución
        boolean resultado = pasajeroService.sendPasswordReset(inputValido.getEmail());

        // A (Assert): Verificación
        assertTrue(resultado, "Debe devolver true si el envío es exitoso.");

        // Verificar que se haya llamado a la función de envío de correo (el efecto)
        verify(pasajeroService, times(1)).sendEmail(eq(inputValido.getEmail()), anyString(), contains("http://frontend/reset-password?token="));

        // Verificar que se haya creado un token en el mapa interno (probando el efecto de la lógica)
        assertTrue(pasajeroService.resetTokens.size() > 0, "El token debe haberse agregado al mapa interno.");
    }

    @Test
    void testResetPassword_CPHU0403_ActualizacionExitosa() {
        // A (Arrange): Preparación
        String token = UUID.randomUUID().toString();
        String newPassword = "NuevaPass2025";

        // 1. Simular el estado interno: Simular que sendPasswordReset ya se ejecutó y generó el token.
        // NOTA: Accedemos al mapa que era privado. Si el campo fue cambiado a 'protected' o 'package-private' en el Service:
        pasajeroService.resetTokens.put(token, pasajeroExistente.getUsername());

        // 2. Mockear la búsqueda y el guardado
        when(repo.findByUsername(pasajeroExistente.getUsername())).thenReturn(Optional.of(pasajeroExistente));
        when(passwordEncoder.encode(newPassword)).thenReturn("NEW_HASHED_PASSWORD");

        // A (Act): Ejecución
        pasajeroService.resetPassword(token, newPassword);

        // A (Assert): Verificación
        // 1. Verificar el cambio de contraseña:
        assertEquals("NEW_HASHED_PASSWORD", pasajeroExistente.getPasswordHash(), "El hash debe ser el nuevo.");

        // 2. Verificar las interacciones:
        verify(repo, times(1)).save(pasajeroExistente);

        // 3. Verificar que el token fue removido:
        assertNull(pasajeroService.resetTokens.get(token), "El token debe ser removido después del uso.");
    }
}
