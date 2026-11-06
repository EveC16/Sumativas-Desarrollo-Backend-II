package com.petsalud.backendpetsalud.security;

import com.petsalud.backendpetsalud.security.UsuarioDetailsService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF deshabilitado para poder usar el JWT  
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No guarda sesiones en el servidor
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/usuarios/registro", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/usuarios/registro", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll() // Consultar productos publico
                        .requestMatchers("/api/admin/**").hasAuthority("GERENTE")
                        .requestMatchers("/api/veterinario/**").hasAnyAuthority("EMPLEADO", "GERENTE")
                        .requestMatchers("/api/propietario/**").hasAuthority("CLIENTE")
                        .anyRequest().denyAll()) 

                        // Configuración excepciones
                        .exceptionHandling(ex -> ex
                            .authenticationEntryPoint((req, res, e) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                            .accessDeniedHandler((req, res, e) -> res.setStatus(HttpServletResponse.SC_FORBIDDEN))
                        
                        )
                            // Busca los usuarios en la base de datos 
                            .userDetailsService(usuarioDetailsService)
                            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Necesario para la autenticación en AuthController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
