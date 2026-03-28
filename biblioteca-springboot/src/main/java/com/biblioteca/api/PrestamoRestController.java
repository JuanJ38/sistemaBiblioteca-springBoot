package com.biblioteca.api;

import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.mapper.PrestamoMapper;
import com.biblioteca.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Prestamos", description = "API REST para gestión de préstamos")
public class PrestamoRestController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private PrestamoMapper prestamoMapper;

    @GetMapping
    @Operation(summary = "Obtener todos los préstamos activos")
    public ResponseEntity<List<PrestamoDTO>> listar() {
        List<PrestamoDTO> prestamos = prestamoService.obtenerActivos()
                .stream().map(prestamoMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(prestamos);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo préstamo")
    public ResponseEntity<String> prestar(@RequestParam Integer idLibro,
                                           @RequestParam Integer idUsuario) {
        boolean exito = prestamoService.prestarLibro(idLibro, idUsuario);
        if (exito) {
            return ResponseEntity.ok("Préstamo registrado correctamente");
        }
        return ResponseEntity.badRequest().body("El libro no está disponible o no existe");
    }

    @PutMapping("/devolver/{id}")
    @Operation(summary = "Devolver un libro prestado")
    public ResponseEntity<String> devolver(@PathVariable Integer id) {
        boolean exito = prestamoService.devolverLibro(id);
        if (exito) {
            return ResponseEntity.ok("Libro devuelto correctamente");
        }
        return ResponseEntity.badRequest().body("No se pudo registrar la devolución");
    }
}
