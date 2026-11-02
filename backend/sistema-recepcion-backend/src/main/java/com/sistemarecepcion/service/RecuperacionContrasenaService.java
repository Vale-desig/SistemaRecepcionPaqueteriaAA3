package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.RecuperacionContrasena;
import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.repository.RecuperacionContrasenaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service 
public class RecuperacionContrasenaService {

    private final RecuperacionContrasenaRepository recuperacionRepository;
    private final UsuarioService usuarioService;
    private final CorreoService correoService;

    private final BCryptPasswordEncoder passwordEncoder =
    new BCryptPasswordEncoder();

    private final SecureRandom random = new SecureRandom();

    public RecuperacionContrasenaService(
        RecuperacionContrasenaRepository recuperacionRepository,
        UsuarioService usuarioService,
        CorreoService correoService) {

            this.recuperacionRepository = recuperacionRepository;
            this.usuarioService = usuarioService;
            this.correoService = correoService;
        }

        @Transactional 
        public void solicitarCodigo(String cedula) {

            Optional<Usuario> usuarioEncontrado =
            usuarioService.buscarPorCedula(cedula);

            if (usuarioEncontrado.isEmpty()) {
                return;
            }

            Usuario usuario = usuarioEncontrado.get();

            // Solo permite recuperar cuentas de residentes activadas.
            if (usuario.getIdRol() == null
                    || usuario.getIdRol() != 3
                    || usuario.getUsuaContrasena() == null
                    || usuario.getUsuaCorreo() == null
                    || usuario.getUsuaCorreo().isBlank()) {
                    return;
                    }

                    // Invalidar códigos anteriores.
                    List<RecuperacionContrasena> anteriores =
                    recuperacionRepository
                    .findByCedulaUsuarioAndUsadoFalse(cedula);

                    for (RecuperacionContrasena anterior : anteriores) {
                        anterior.setUsado(true);
                    }

                    recuperacionRepository.saveAll(anteriores);

                    // Generar código numérico de seis dígitos.
                    String codigo = String.format("%06d", random.nextInt(1000000));

                    RecuperacionContrasena recuperacion =
                    new RecuperacionContrasena();

                    recuperacion.setCedulaUsuario(cedula);
                    recuperacion.setCodigoHash(passwordEncoder.encode(codigo));
                    recuperacion.setFechaExpiracion(
                        LocalDateTime.now().plusMinutes(10));
                        recuperacion.setUsado(false);

                        recuperacionRepository.save(recuperacion);

                        // Enviar el código al correo registrado.
                        correoService.enviarCodigoRecuperacion(
                            usuario.getUsuaCorreo(), codigo);
        }

        @Transactional 
        public boolean verificarCodigo(String cedula, String codigo) {

            List<RecuperacionContrasena> recuperaciones =
            recuperacionRepository
            .findByCedulaUsuarioAndUsadoFalse(cedula);

            LocalDateTime ahora = LocalDateTime.now();

            for (RecuperacionContrasena recuperacion : recuperaciones) {

                if (recuperacion.getFechaExpiracion().isAfter(ahora)
                && passwordEncoder.matches(
            codigo, recuperacion.getCodigoHash())) {
                return true;
            }
            }

            return false;
        }

        @Transactional 
        public boolean cambiarContrasena(
            String cedula, String codigo, String nuevaContrasena) {

                if (nuevaContrasena == null
                    || nuevaContrasena.length() < 8) {
                        return false;
                    }

                    Optional<Usuario> usuarioEncontrado =
                    usuarioService.buscarPorCedula(cedula);

                    if (usuarioEncontrado.isEmpty()) {
                        return false;
                    }

                    if (!verificarCodigo(cedula, codigo)) {
                        return false;
                    }

                    Usuario usuario = usuarioEncontrado.get();

                    if (usuario.getIdRol() == null
                || usuario.getIdRol() != 3) {
                    return false;
                }

                usuario.setUsuaContrasena(
                    passwordEncoder.encode(nuevaContrasena));

                    usuarioService.guardarUsuario(usuario);

                    List<RecuperacionContrasena> recuperaciones =
                    recuperacionRepository
                    .findByCedulaUsuarioAndUsadoFalse(cedula);

                    for (RecuperacionContrasena recuperacion : recuperaciones) {
                        recuperacion.setUsado(true);
                    }

                    recuperacionRepository.saveAll(recuperaciones);

                    return true;
            }
    
}
