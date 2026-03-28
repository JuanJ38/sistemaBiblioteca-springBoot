package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("libros", libroService.obtenerTodos());
        return "libros/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        // Usar builder para que disponible=true sea inicializado correctamente
        model.addAttribute("libro", Libro.builder().disponible(true).build());
        return "libros/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("libro") Libro libro,
                          BindingResult result,
                          RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "libros/formulario";
        }
        libroService.guardar(libro);
        redirectAttrs.addFlashAttribute("exito", "Libro guardado correctamente");
        return "redirect:/libros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
        return libroService.buscarPorId(id)
                .map(libro -> {
                    model.addAttribute("libro", libro);
                    return "libros/formulario";
                })
                .orElseGet(() -> {
                    redirectAttrs.addFlashAttribute("error", "Libro no encontrado");
                    return "redirect:/libros";
                });
    }

    @PostMapping("/actualizar")
    public String actualizar(@Valid @ModelAttribute("libro") Libro libro,
                             BindingResult result,
                             RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "libros/formulario";
        }
        libroService.guardar(libro);
        redirectAttrs.addFlashAttribute("exito", "Libro actualizado correctamente");
        return "redirect:/libros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        libroService.eliminar(id);
        redirectAttrs.addFlashAttribute("exito", "Libro eliminado correctamente");
        return "redirect:/libros";
    }
}
