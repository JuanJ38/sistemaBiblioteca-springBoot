package com.biblioteca.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioDTO {
    private Integer id;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo no tiene formato válido")
    private String correo;
}
