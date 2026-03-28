package com.biblioteca.controller;

import com.biblioteca.service.LibroService;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar préstamos activos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerActivos());
        return "prestamos/lista";
    }

    // Mostrar formulario para prestar
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libros", libroService.obtenerDisponibles());
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "prestamos/formulario";
    }

    // Registrar préstamo
    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idLibro,
                          @RequestParam Integer idUsuario,
                          RedirectAttributes redirectAttrs) {
        boolean exito = prestamoService.prestarLibro(idLibro, idUsuario);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Préstamo registrado correctamente");
        } else {
            redirectAttrs.addFlashAttribute("error", "El libro no está disponible o no existe");
        }
        return "redirect:/prestamos";
    }

    // Devolver libro
    @GetMapping("/devolver/{id}")
    public String devolver(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        boolean exito = prestamoService.devolverLibro(id);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Libro devuelto correctamente");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo registrar la devolución");
        }
        return "redirect:/prestamos";
    }
}
