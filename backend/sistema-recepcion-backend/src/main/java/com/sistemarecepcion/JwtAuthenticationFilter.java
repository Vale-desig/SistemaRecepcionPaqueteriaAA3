package com.sistemarecepcion;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override 
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

            String encabezado = request.getHeader("Authorization");

            if (encabezado != null && encabezado.startsWith("Bearer ")) {
                String token = encabezado.substring(7);

                try {
                    Claims claims = jwtService.validarToken(token);

                    String idUsuario = claims.getSubject();
                    Integer idRol = claims.get("idRol", Integer.class);

                    String autoridad = switch (idRol) {
                        case 1 -> "ROLE_ADMIN";
                        case 2 -> "ROLE_PORTERO";
                        case 3 -> "ROLE_RESIDENTE";
                        default -> null;
                    };

                    if (autoridad != null
                        && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

                            var authentication =
                            new UsernamePasswordAuthenticationToken(
                                idUsuario,
                            null,
                        List.of(new SimpleGrantedAuthority(autoridad))
                    );

                    SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
                    }

                } catch (io.jsonwebtoken.JwtException
                    | IllegalArgumentException e) {

                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write(
                            "{\"mensaje\":\"Token inválido o vencido.\"}"
                        );
                        return;
                    }
            }

            filterChain.doFilter(request, response);
        }
    
}
