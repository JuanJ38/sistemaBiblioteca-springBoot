package com.biblioteca.api;

import com.biblioteca.dto.LibroDTO;
import com.biblioteca.mapper.LibroMapper;
import com.biblioteca.model.Libro;
import com.biblioteca.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "API REST para gestión de libros")
public class LibroRestController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private LibroMapper libroMapper;

    @GetMapping
    @Operation(summary = "Obtener todos los libros")
    public ResponseEntity<List<LibroDTO>> listar() {
        List<LibroDTO> libros = libroService.obtenerTodos()
                .stream().map(libroMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener libros disponibles")
    public ResponseEntity<List<LibroDTO>> disponibles() {
        List<LibroDTO> libros = libroService.obtenerDisponibles()
                .stream().map(libroMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar libro por ID")
    public ResponseEntity<LibroDTO> buscarPorId(@PathVariable Integer id) {
        return libroService.buscarPorId(id)
                .map(libro -> ResponseEntity.ok(libroMapper.toDTO(libro)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar libros por título")
    public ResponseEntity<List<LibroDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<LibroDTO> libros = libroService.buscarPorTitulo(titulo)
                .stream().map(libroMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar libros con paginación")
    public ResponseEntity<Page<LibroDTO>> listarPaginado(Pageable pageable) {
        Page<LibroDTO> pagina = libroService.obtenerPaginado(pageable)
                .map(libroMapper::toDTO);
        return ResponseEntity.ok(pagina);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro")
    public ResponseEntity<LibroDTO> crear(@Valid @RequestBody LibroDTO dto) {
        Libro libro = libroMapper.toEntity(dto);
        Libro guardado = libroService.guardar(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libroMapper.toDTO(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro existente")
    public ResponseEntity<LibroDTO> actualizar(@PathVariable Integer id,
                                                @Valid @RequestBody LibroDTO dto) {
        return libroService.buscarPorId(id).map(libro -> {
            libro.setTitulo(dto.getTitulo());
            libro.setAutor(dto.getAutor());
            libro.setDisponible(dto.isDisponible());
            return ResponseEntity.ok(libroMapper.toDTO(libroService.guardar(libro)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (libroService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
