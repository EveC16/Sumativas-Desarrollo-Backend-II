// Configuración de JwtUtil para generar y validar tokens
package com.petsalud.backendpetsalud.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {

    private final SecretKey secretKey;
    private final String issuer;
    private final long expirationMinutes;

    public JwtService(    
        @Value("${demo.jwt.secret}") String base64Secret,
        @Value("${demo.jwt.issuer}") String issuer,
        @Value("${demo.jwt.expiration-minutes}") long expirationMinutes) {
            
            // Se recibe el secret y se decodifica si cumple
            byte[] decoded = Base64.getDecoder().decode(base64Secret);
            if(decoded.length < 32) throw new IllegalArgumentException("JWT_SECRET_BASE64 requiere al menos 32 bytes");
            this.secretKey = Keys.hmacShaKeyFor(decoded);
            this.issuer = issuer;
            this.expirationMinutes = expirationMinutes;
            }

    // Metodo para generar token con emisor, usuario, rol y tiempo de expiracion
    public String generateToken(UserDetails user){

        Instant now = Instant.now(); // Obtiene el tiempo
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(); // Obtiene los roles del usuario
        return Jwts.builder().issuer(issuer).subject(user.getUsername()) // Arma el JWT
            .claim("roles", roles) // Pasa el rol 
            .issuedAt(Date.from(now)) // Calcula la fecha de inicio
            .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES))) // Calcula el tiempo de expiración 
            .signWith(secretKey, Jwts.SIG.HS256).compact(); // Pasa la firma

    }

    // Metodo para validar token
    public Claims validateToken(String token){
    Claims claims = Jwts.parser().verifyWith(secretKey).requireIssuer(issuer)
                    .build().parseSignedClaims(token).getPayload(); // Obtiene los claims

    if(claims.getSubject() == null || claims.getSubject().isBlank()){
      throw new IllegalArgumentException("Token sin identificador");
    }
    return claims;
  }
}
