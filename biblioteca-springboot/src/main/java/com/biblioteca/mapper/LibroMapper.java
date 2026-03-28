package com.biblioteca.mapper;
import com.biblioteca.dto.LibroDTO;
import com.biblioteca.model.Libro;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LibroMapper {
    LibroDTO toDTO(Libro libro);
    Libro toEntity(LibroDTO dto);
}
