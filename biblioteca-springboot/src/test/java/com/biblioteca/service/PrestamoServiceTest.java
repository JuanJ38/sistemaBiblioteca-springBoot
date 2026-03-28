package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock private PrestamoRepository prestamoRepository;
    @Mock private LibroRepository libroRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    private Libro libroDisponible;
    private Libro libroNoDisponible;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        libroDisponible = Libro.builder().id(1).titulo("El Principito").autor("Saint-Exupéry").disponible(true).build();
        libroNoDisponible = Libro.builder().id(2).titulo("1984").autor("Orwell").disponible(false).build();
        usuario = Usuario.builder().id(1).nombre("Juan Pérez").correo("juan@mail.com").build();
    }

    @Test
    void prestarLibro_conLibroDisponible_debeRetornarTrue() {
        when(libroRepository.findById(1)).thenReturn(Optional.of(libroDisponible));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(i -> i.getArgument(0));
        when(libroRepository.save(any(Libro.class))).thenAnswer(i -> i.getArgument(0));

        boolean resultado = prestamoService.prestarLibro(1, 1);

        assertTrue(resultado);
        assertFalse(libroDisponible.isDisponible());
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void prestarLibro_conLibroNoDisponible_debeRetornarFalse() {
        when(libroRepository.findById(2)).thenReturn(Optional.of(libroNoDisponible));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        boolean resultado = prestamoService.prestarLibro(2, 1);

        assertFalse(resultado);
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void prestarLibro_conLibroInexistente_debeRetornarFalse() {
        when(libroRepository.findById(99)).thenReturn(Optional.empty());
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        boolean resultado = prestamoService.prestarLibro(99, 1);

        assertFalse(resultado);
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void devolverLibro_conPrestamoActivo_debeRetornarTrue() {
        Prestamo prestamo = Prestamo.builder()
                .id(1).libro(libroNoDisponible).usuario(usuario)
                .fechaPrestamo(LocalDate.now().minusDays(3))
                .fechaDevolucion(null).build();

        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(i -> i.getArgument(0));
        when(libroRepository.save(any(Libro.class))).thenAnswer(i -> i.getArgument(0));

        boolean resultado = prestamoService.devolverLibro(1);

        assertTrue(resultado);
        assertNotNull(prestamo.getFechaDevolucion());
        assertTrue(libroNoDisponible.isDisponible());
    }

    @Test
    void devolverLibro_conPrestamoInexistente_debeRetornarFalse() {
        when(prestamoRepository.findById(99)).thenReturn(Optional.empty());

        boolean resultado = prestamoService.devolverLibro(99);

        assertFalse(resultado);
        verify(prestamoRepository, never()).save(any());
    }
}
