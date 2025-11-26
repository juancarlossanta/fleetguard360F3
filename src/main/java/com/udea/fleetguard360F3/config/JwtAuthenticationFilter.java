package com.udea.fleetguard360F3.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("REQUEST URI: " + request.getRequestURI());
        System.out.println("Authorization Header: " + header);

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);
            System.out.println("Token extraído: " + token.substring(0, 20) + "...");
            String username = jwtUtil.extractUsername(token);
            System.out.println("Username del token: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("UserDetails cargado: " + userDetails.getUsername());

                if (jwtUtil.validateToken(token, userDetails)) {
                    System.out.println("Token válido, estableciendo autenticación");

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticación establecida en SecurityContext");
                } else {
                    System.out.println("Token inválido");
                }
            } else {
                System.out.println("Username null o ya hay autenticación");
            }
        } else {
            System.out.println("No hay header Authorization ");
        }


        filterChain.doFilter(request, response);
    }
}
