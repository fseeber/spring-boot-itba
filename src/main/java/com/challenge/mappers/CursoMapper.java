package com.challenge.mappers;

import com.challenge.dtos.CursoDto;
import com.challenge.entities.Curso;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CursoMapper {

    @Mapping(target = "id", ignore = true)
    Curso toEntity(CursoDto dto);

    CursoDto toDto(Curso entity);
    
    List<CursoDto> toDtoList(List<Curso> entities);
}
