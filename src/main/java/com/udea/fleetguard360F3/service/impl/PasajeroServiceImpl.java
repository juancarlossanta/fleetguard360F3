package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.dto.RegistroPasajeroDto;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.repository.PasajeroRepository;
import com.udea.fleetguard360F3.service.EmailService;
import com.udea.fleetguard360F3.service.PasajeroService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class PasajeroServiceImpl implements PasajeroService {
    private final PasajeroRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Key jwtKey;
    private final EmailService emailService;

    public PasajeroServiceImpl(PasajeroRepository repo, BCryptPasswordEncoder passwordEncoder, Key jwtKey, EmailService emailService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtKey = jwtKey;
        this.emailService = emailService;
    }
    @Override
    @Transactional
    public Pasajero register(RegistroPasajeroDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        if (repo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso.");
        }

        Pasajero p = new Pasajero();
        p.setUsername(dto.getUsername());
        p.setNombre(dto.getNombre());
        p.setApellido(dto.getApellido());
        p.setIdentificacion(dto.getIdentificacion());
        p.setTelefono(dto.getTelefono());
        p.setEmail(dto.getEmail());
        p.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return repo.save(p);
    }

    @Override
    public boolean emailAvailable(String email) {
        return !repo.existsByEmail(email);
    }

    @Override
    public boolean usernameAvailable(String username) {
        return !repo.existsByUsername(username);
    }

    @Override
    public Pasajero findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Pasajero authenticate(String username, String password) {
        Pasajero p = repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, p.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        return p;
    }

    @Override
    public String generateToken(Pasajero pasajero) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(pasajero.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60))
                .signWith(jwtKey)
                .compact();
    }


    @Override
    public boolean sendPasswordReset(String email) {
        Optional<Pasajero> optional  = repo.findByEmail(email);

        if (optional.isEmpty())return false;

        Pasajero p = optional.get();

        String token = UUID.randomUUID().toString();
        // Guardar token y expiración en base de datos
        p.setResetToken(token);
        p.setResetTokenExpiry(Instant.now().plus(15, ChronoUnit.MINUTES)); // 15 minutos
        repo.save(p);

        // URL del frontend
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        // Enviar correo
        String asunto = " Restablecimiento de contraseña - FleetGuard360";
        String cuerpo = String.format(
                "Hola %s,\n\n" +
                        "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                        "Haz clic en el siguiente enlace para crear una nueva contraseña:\n" +
                        "%s\n\n" +
                        "Este enlace expirará en 15 minutos.\n\n" +
                        "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
                        "Saludos,\n" +
                        "Equipo FleetGuard360",
                p.getNombre(),
                resetUrl
        );
        try {
            emailService.sendEmailWithRetry(p.getEmail(), asunto, cuerpo);
            System.out.println(" Correo de restablecimiento enviado a: " + p.getEmail());
            return true;
        } catch (Exception e) {
            System.err.println(" Error al enviar correo de restablecimiento: " + e.getMessage());
            // Limpiar token si falla el envío
            p.setResetToken(null);
            p.setResetTokenExpiry(null);
            repo.save(p);
            return false;
        }
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        Pasajero p = repo.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o expirado"));

        // Verificar si el token ha expirado
        if (p.getResetTokenExpiry() == null || Instant.now().isAfter(p.getResetTokenExpiry())) {
            throw new IllegalArgumentException("El token ha expirado");
        }

        // Validar contraseña
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        // Actualizar contraseña
        p.setPasswordHash(passwordEncoder.encode(newPassword));

        // Limpiar token
        p.setResetToken(null);
        p.setResetTokenExpiry(null);

        repo.save(p);

        System.out.println(" Contraseña actualizada para: " + p.getUsername());
    }
}
