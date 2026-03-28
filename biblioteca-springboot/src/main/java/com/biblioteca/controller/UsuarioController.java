package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", Usuario.builder().build());
        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }
        usuarioService.guardar(usuario);
        redirectAttrs.addFlashAttribute("exito", "Usuario guardado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "usuarios/formulario";
                })
                .orElseGet(() -> {
                    redirectAttrs.addFlashAttribute("error", "Usuario no encontrado");
                    return "redirect:/usuarios";
                });
    }

    @PostMapping("/actualizar")
    public String actualizar(@Valid @ModelAttribute("usuario") Usuario usuario,
                             BindingResult result,
                             RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }
        usuarioService.guardar(usuario);
        redirectAttrs.addFlashAttribute("exito", "Usuario actualizado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        usuarioService.eliminar(id);
        redirectAttrs.addFlashAttribute("exito", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }
}
