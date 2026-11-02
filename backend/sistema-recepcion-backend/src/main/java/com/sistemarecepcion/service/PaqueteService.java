
package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Paquete;
import com.sistemarecepcion.repository.PaqueteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;

    public PaqueteService(PaqueteRepository paqueteRepository) {
        this.paqueteRepository = paqueteRepository;
    }

    public List<Paquete> listarPaquetes() {
        return paqueteRepository.findAll();
    }

    public List<Paquete> listarPorUsuario(Integer idUsuario) {
        return paqueteRepository.findByIdUsuario(idUsuario);
    }

    public Optional<Paquete> buscarPorId(Integer id) {
        return paqueteRepository.findById(id);
    }

    public Paquete guardarPaquete(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    public void eliminarPaquete(Integer id) {
        paqueteRepository.deleteById(id);
    }
}