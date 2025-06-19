package com.challenge.mappers;

import com.challenge.dtos.MateriaDto;
import com.challenge.entities.Materia;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MateriaMapper {

    @Mapping(target = "id", ignore = true)
    Materia toEntity(MateriaDto dto);

    MateriaDto toDto(Materia entity);
    
    List<MateriaDto> toDtoList(List<Materia> entities);
}
