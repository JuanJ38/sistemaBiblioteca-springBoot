package com.biblioteca.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AuthRequestDTO {
    private String username;
    private String password;
}
