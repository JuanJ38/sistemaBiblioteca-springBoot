package com.biblioteca.repository;

import com.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Buscar libros disponibles
    List<Libro> findByDisponibleTrue();

    // Buscar por título (contiene, ignora mayúsculas)
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
}
