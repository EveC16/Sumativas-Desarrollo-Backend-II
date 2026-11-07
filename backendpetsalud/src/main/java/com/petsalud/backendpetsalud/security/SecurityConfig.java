package com.petsalud.backendpetsalud.security;

import com.petsalud.backendpetsalud.entity.Rol;
import com.petsalud.backendpetsalud.security.UsuarioDetailsService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true) // Definicion de reglas de acceso con @PreAuthorize
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF deshabilitado para poder usar el JWT  
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No guarda sesiones en el servidor
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/usuarios/registro", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll() // Consultar productos publico
                        .requestMatchers("/api/notificaciones/**", "/api/recompensas/**", "/api/usuarios/**").hasAuthority(Rol.ADMIN.name()) // Se traen los roles usando enum
                        .requestMatchers("/api/citas/**", "/api/expedientes**", "/api/mascotas**", "/api/productos**", "/api/ventas/**").hasAnyAuthority(Rol.VETERINARIO.name(), Rol.ADMIN.name())
                        .requestMatchers(HttpMethod.POST,"/api/citas", "/api/mascotas/propietario/{propietarioId}").hasAuthority(Rol.CLIENTE.name()) // Agenda citas, 
                        .requestMatchers(HttpMethod.GET, "/api/recompensas/usuario/{usuarioId}").hasAuthority(Rol.CLIENTE.name()) // Para que el cliente pueda ver las recompensas que tiene.
                        .requestMatchers(HttpMethod.DELETE,"/api/citas/{id}").hasAuthority(Rol.CLIENTE.name()) // Cancela citas,
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
