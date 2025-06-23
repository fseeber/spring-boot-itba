package com.challenge.mappers;

import com.challenge.dtos.AlumnoDto;
import com.challenge.dtos.AlumnosInscriptosDto;
import com.challenge.entities.Alumno;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlumnoMapper {

    Alumno toEntity(AlumnoDto dto);
    AlumnoDto toDto(Alumno entity);
    List<AlumnoDto> toDtoList(List<Alumno> entities);

    AlumnosInscriptosDto toAlumnosInscriptosDto(Alumno entity);
    List<AlumnosInscriptosDto> toAlumnosInscriptosDtoList(List<Alumno> entities);
}