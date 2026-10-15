package com.ejemplo.estudiantes_api.service;

import com.ejemplo.estudiantes_api.exception.RecursoNoEncontradoException;
import com.ejemplo.estudiantes_api.model.Estudiante;
import com.ejemplo.estudiantes_api.repository.EstudianteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository repo;

    // Inyección de dependencias mediante constructor
    public EstudianteService(EstudianteRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Estudiante> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Estudiante consultar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));
    }

    @Transactional
    public Estudiante crear(Estudiante nuevo) {
        nuevo.setId(null); // Asegura que la base de datos asigne el identificador
        if (repo.existsByCorreoIgnoreCase(nuevo.getCorreo())) {
            throw new DataIntegrityViolationException(
                    "Ya existe un estudiante registrado con el correo: " + nuevo.getCorreo());
        }
        return repo.saveAndFlush(nuevo);
    }

    @Transactional
    public Estudiante actualizar(Long id, Estudiante datos) {
        Estudiante actual = consultar(id);

        // Si el correo cambia, validar unicidad
        if (!actual.getCorreo().equalsIgnoreCase(datos.getCorreo())
                && repo.existsByCorreoIgnoreCase(datos.getCorreo())) {
            throw new DataIntegrityViolationException("El correo ya está en uso por otro registro");
        }

        actual.setNombre(datos.getNombre());
        actual.setApellido(datos.getApellido());
        actual.setCorreo(datos.getCorreo());
        actual.setPrograma(datos.getPrograma());

        return repo.saveAndFlush(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        Estudiante actual = consultar(id);
        repo.delete(actual);
    }

    @Transactional(readOnly = true)
    public List<Estudiante> buscarPorPrograma(String programa) {
        return repo.findByProgramaContainingIgnoreCase(programa);
    }
}