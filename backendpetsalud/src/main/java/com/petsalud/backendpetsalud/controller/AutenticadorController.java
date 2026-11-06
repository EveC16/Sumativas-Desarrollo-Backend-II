package com.petsalud.backendpetsalud.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petsalud.backendpetsalud.security.JwtService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

// Recibe el usuario y contraseña que se mando por HTTP POST
@RestController
@RequestMapping("/api/auth")
public class AutenticadorController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    
    // Inyeción de las dependencias de autenticación.
    public AutenticadorController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    // Valida credenciales sin devolverlas al consumidor.
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())); // Entrega username y password a la variable de autenticacion.
            UserDetails user = (UserDetails) auth.getPrincipal(); // Se guarda en la variable el usuario que intenta registrarse
            return ResponseEntity.ok(Map.of("token", jwtService.generateToken(user), "type", "Bearer")); // Una vez validado, se entrega el usuario a JWT
        } catch (BadCredentialsException ex) { 
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {} // Gestiona el usuario y la contraseña
    
}
