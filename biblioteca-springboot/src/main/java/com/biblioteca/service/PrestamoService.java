package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Prestamo> obtenerTodos() {
        return prestamoRepository.findAll();
    }

    public List<Prestamo> obtenerActivos() {
        return prestamoRepository.findByFechaDevolucionIsNull();
    }

    @Transactional
    public boolean prestarLibro(Integer idLibro, Integer idUsuario) {
        Optional<Libro> libroOpt = libroRepository.findById(idLibro);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (libroOpt.isEmpty() || usuarioOpt.isEmpty()) {
            return false;
        }

        Libro libro = libroOpt.get();
        Usuario usuario = usuarioOpt.get();

        if (!libro.isDisponible()) {
            return false;
        }

        // Usar builder en vez de constructor directo
        Prestamo prestamo = Prestamo.builder()
                .libro(libro)
                .usuario(usuario)
                .fechaPrestamo(LocalDate.now())
                .build();

        prestamoRepository.save(prestamo);

        libro.setDisponible(false);
        libroRepository.save(libro);

        return true;
    }

    @Transactional
    public boolean devolverLibro(Integer idPrestamo) {
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(idPrestamo);

        if (prestamoOpt.isEmpty()) {
            return false;
        }

        Prestamo prestamo = prestamoOpt.get();

        if (prestamo.getFechaDevolucion() != null) {
            return false;
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamoRepository.save(prestamo);

        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroRepository.save(libro);

        return true;
    }
}
