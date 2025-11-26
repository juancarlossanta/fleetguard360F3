package com.udea.fleetguard360F3.config;

import com.udea.fleetguard360F3.model.Pasajero;
import com.udea.fleetguard360F3.repository.PasajeroRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasajeroRepository pasajeroRepository;

    public CustomUserDetailsService(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Pasajero pasajero = pasajeroRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(pasajero.getUsername())
                .password(pasajero.getPasswordHash())
                .roles("USER")
                .build();
    }
}

