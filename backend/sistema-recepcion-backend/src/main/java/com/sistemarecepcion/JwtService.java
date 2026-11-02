package com.sistemarecepcion;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service 
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(Integer idUsuario, Integer idRol) {
        Date ahora = new Date();
        Date vencimiento = new Date(ahora.getTime() + expiration);

        return Jwts.builder()
        .subject(idUsuario.toString())
        .claim("idRol", idRol)
        .issuedAt(ahora)
        .expiration(vencimiento)
        .signWith(getSigningKey())
        .compact();
    }

    public Claims validarToken(String token) {
        return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }
    
}
