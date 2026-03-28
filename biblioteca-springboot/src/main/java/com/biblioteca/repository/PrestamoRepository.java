package com.biblioteca.repository;

import com.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    // Préstamos activos (sin fecha de devolución)
    List<Prestamo> findByFechaDevolucionIsNull();

    // Préstamos de un usuario específico
    List<Prestamo> findByUsuarioId(Integer idUsuario);
}
