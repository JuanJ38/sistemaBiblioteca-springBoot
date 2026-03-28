package com.biblioteca.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LibroDTO {
    private Integer id;
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;
    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;
    private boolean disponible;
}
