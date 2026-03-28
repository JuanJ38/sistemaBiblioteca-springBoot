package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Guardar
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Eliminar
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
