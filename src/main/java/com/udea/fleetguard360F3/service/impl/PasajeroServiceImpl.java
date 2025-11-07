package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.dto.RegistroPasajeroDto;
import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.repository.PasajeroRepository;
import com.udea.fleetguard360F3.service.PasajeroService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
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
    public final Map<String, String> resetTokens = new HashMap<>();
    private static final long EXPIRATION = (long) 1000 * 60 * 15; // 15 min

    public PasajeroServiceImpl(PasajeroRepository repo, BCryptPasswordEncoder passwordEncoder, Key jwtKey) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtKey = jwtKey;
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
        resetTokens.put(token, p.getUsername());

        sendEmail(email, "Restablecer contraseña",
                "Link: http://frontend/reset-password?token=" + token);

        new Timer().schedule(new TimerTask() {
            public void run() {
                resetTokens.remove(token);
            }
        }, EXPIRATION);

        return true;
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        String username = resetTokens.get(token);
        if (username == null) throw new IllegalArgumentException("Token inválido o expirado");

        Pasajero p = repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        p.setPasswordHash(passwordEncoder.encode(newPassword));
        repo.save(p);
        resetTokens.remove(token);
    }

    public void sendEmail(String to, String subject, String body) {
        System.out.println("Enviar email a " + to + " con body: " + body);
    }
}
