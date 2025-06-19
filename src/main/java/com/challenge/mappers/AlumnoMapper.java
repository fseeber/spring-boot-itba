package com.challenge.mappers;

import com.challenge.dtos.AlumnoDto;
import com.challenge.entities.Alumno;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlumnoMapper {

    @Mapping(target = "id", ignore = true)
    Alumno toEntity(AlumnoDto dto);

    AlumnoDto toDto(Alumno entity);
    
    List<AlumnoDto> toDtoList(List<Alumno> entities);
}