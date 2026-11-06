// Clase encargada de interceptar la solicitud para revisar si el usuario trae un Token valido. 

package com.petsalud.backendpetsalud.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class JwtFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService users;

    public JwtFilter(JwtService jwtService, UserDetailsService users) {
        this.jwtService = jwtService;
        this.users = users;
    }

    // Extre y verifica extructura del token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)throws ServletException, IOException {

        String header = request.getHeader("Authorization"); // Saca el Header del Request
            if(header != null && header.startsWith("Bearer")){ // Valida peticiones nulas o anonimas y que venga Bearer en la cabecera para que solo procese autenticacion por token
            try {
                Claims claims = jwtService.validateToken(header.substring(7));
                UserDetails user = users.loadUserByUsername(claims.getSubject());
                if (user.isEnabled() && user.isAccountNonLocked() && user.isAccountNonExpired()) { // Si el usuario esta activo y no esta bloqueado
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // Revisar si colocar roles
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException ex) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

}
