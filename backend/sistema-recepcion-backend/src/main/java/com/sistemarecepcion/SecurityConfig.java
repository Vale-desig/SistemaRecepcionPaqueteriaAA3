
package com.sistemarecepcion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()

                // Consulta de usuarios para administrador y portero
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**")
                    .hasAnyRole("ADMIN", "PORTERO")

                // Las demás operaciones de usuarios son solo para administrador
                .requestMatchers("/api/usuarios/**")
                    .hasRole("ADMIN")

                // Todos los roles pueden consultar paquetes
                .requestMatchers(HttpMethod.GET, "/api/paquetes/**")
                    .hasAnyRole("ADMIN", "PORTERO", "RESIDENTE")

                // Administrador y portero pueden registrar y modificar paquetes
                .requestMatchers(HttpMethod.POST, "/api/paquetes/**")
                    .hasAnyRole("ADMIN", "PORTERO")
                .requestMatchers(HttpMethod.PUT, "/api/paquetes/**")
                    .hasAnyRole("ADMIN", "PORTERO")

                // Solo administrador puede eliminar paquetes
                .requestMatchers(HttpMethod.DELETE, "/api/paquetes/**")
                    .hasRole("ADMIN")

                    // Solo administrador y portero pueden consultar entregas
                    .requestMatchers(HttpMethod.GET, "/api/entregas/**")
                    .hasAnyRole("ADMIN", "PORTERO")

                    // Solo administrador y portero pueden registrar entregas
                    .requestMatchers(HttpMethod.POST, "/api/entregas/**")
                    .hasAnyRole("ADMIN", "PORTERO")

                    // Solo el administrador puede consultar todos los incidentes
                    .requestMatchers(HttpMethod.GET, "/api/incidentes/**")
                    .hasRole("ADMIN")

                    // Solo el portero puede registrar incidentes
                    .requestMatchers(HttpMethod.POST, "/api/incidentes/**")
                    .hasRole("PORTERO")

                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(
            List.of("Authorization", "Content-Type")
        );
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}