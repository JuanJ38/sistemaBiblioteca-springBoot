package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        libro1 = Libro.builder().id(1).titulo("Cien años de soledad").autor("García Márquez").disponible(true).build();
        libro2 = Libro.builder().id(2).titulo("1984").autor("George Orwell").disponible(false).build();
    }

    @Test
    void obtenerTodos_debeRetornarListaDeLibros() {
        when(libroRepository.findAll()).thenReturn(Arrays.asList(libro1, libro2));

        List<Libro> resultado = libroService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void obtenerDisponibles_debeRetornarSoloLibrosDisponibles() {
        when(libroRepository.findByDisponibleTrue()).thenReturn(Arrays.asList(libro1));

        List<Libro> resultado = libroService.obtenerDisponibles();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isDisponible());
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarLibro() {
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro1));

        Optional<Libro> resultado = libroService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Cien años de soledad", resultado.get().getTitulo());
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarVacio() {
        when(libroRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Libro> resultado = libroService.buscarPorId(99);

        assertFalse(resultado.isPresent());
    }

    @Test
    void guardar_debeRetornarLibroGuardado() {
        Libro nuevoLibro = Libro.builder().titulo("El Principito").autor("Saint-Exupéry").disponible(true).build();
        Libro libroGuardado = Libro.builder().id(3).titulo("El Principito").autor("Saint-Exupéry").disponible(true).build();

        // Usar any(Libro.class) para evitar problemas con equals basado solo en id
        when(libroRepository.save(any(Libro.class))).thenReturn(libroGuardado);

        Libro resultado = libroService.guardar(nuevoLibro);

        assertNotNull(resultado.getId());
        assertEquals("El Principito", resultado.getTitulo());
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    void eliminar_debeInvocarDeleteById() {
        doNothing().when(libroRepository).deleteById(1);

        libroService.eliminar(1);

        verify(libroRepository, times(1)).deleteById(1);
    }

    @Test
    void buscarPorTitulo_debeRetornarLibrosCoincidentes() {
        when(libroRepository.findByTituloContainingIgnoreCase("1984")).thenReturn(Arrays.asList(libro2));

        List<Libro> resultado = libroService.buscarPorTitulo("1984");

        assertEquals(1, resultado.size());
        assertEquals("1984", resultado.get(0).getTitulo());
    }
}
