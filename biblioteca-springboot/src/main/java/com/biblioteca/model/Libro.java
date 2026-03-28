package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Libro {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    @Column(nullable = false)
    private String autor;

    @Builder.Default
    @Column(nullable = false)
    private boolean disponible = true;
}
