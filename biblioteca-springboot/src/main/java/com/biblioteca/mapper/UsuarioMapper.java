package com.biblioteca.mapper;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO dto);
}
