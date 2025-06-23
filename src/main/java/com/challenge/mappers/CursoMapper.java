package com.challenge.mappers;

import com.challenge.dtos.CursoDto;
import com.challenge.dtos.CursoConAlumnosDto;
import com.challenge.entities.Curso;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {AlumnoMapper.class})
public interface CursoMapper {

    Curso toEntity(CursoDto dto);
    CursoDto toDto(Curso entity);
    List<CursoDto> toDtoList(List<Curso> entities);

    @Mapping(target = "alumnosInscriptos", source = "alumnosInscriptos")
    CursoConAlumnosDto toCursoConAlumnosDto(Curso entity);
    List<CursoConAlumnosDto> toCursoConAlumnosDtoList(List<Curso> entities);
}
