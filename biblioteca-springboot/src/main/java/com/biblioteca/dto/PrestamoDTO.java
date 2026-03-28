package com.biblioteca.dto;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PrestamoDTO {
    private Integer id;
    private String tituloLibro;
    private String nombreUsuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
}
