package com.biblioteca.mapper;
import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.model.Prestamo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {
    @Mapping(source = "libro.titulo", target = "tituloLibro")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    PrestamoDTO toDTO(Prestamo prestamo);
}
