package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> obtenerTodos() { return libroRepository.findAll(); }

    public List<Libro> obtenerDisponibles() { return libroRepository.findByDisponibleTrue(); }

    public Optional<Libro> buscarPorId(Integer id) { return libroRepository.findById(id); }

    public Libro guardar(Libro libro) { return libroRepository.save(libro); }

    public void eliminar(Integer id) { libroRepository.deleteById(id); }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Page<Libro> obtenerPaginado(Pageable pageable) {
        return libroRepository.findAll(pageable);
    }

    public void actualizarDisponibilidad(Integer idLibro, boolean disponible) {
        libroRepository.findById(idLibro).ifPresent(libro -> {
            libro.setDisponible(disponible);
            libroRepository.save(libro);
        });
    }
}
