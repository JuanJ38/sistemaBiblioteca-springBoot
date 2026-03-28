package com.biblioteca.api;

import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.mapper.UsuarioMapper;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API REST para gestión de usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos()
                .stream().map(usuarioMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
                .map(u -> ResponseEntity.ok(usuarioMapper.toDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        Usuario guardado = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toDTO(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Integer id,
                                                  @Valid @RequestBody UsuarioDTO dto) {
        return usuarioService.buscarPorId(id).map(usuario -> {
            usuario.setNombre(dto.getNombre());
            usuario.setCorreo(dto.getCorreo());
            return ResponseEntity.ok(usuarioMapper.toDTO(usuarioService.guardar(usuario)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (usuarioService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
