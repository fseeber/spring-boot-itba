package com.challenge.mappers;

import com.challenge.dtos.AulaDto;
import com.challenge.entities.Aula;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AulaMapper {

    @Mapping(target = "id", ignore = true)
    Aula toEntity(AulaDto dto);

    AulaDto toDto(Aula entity);
    
    List<AulaDto> toDtoList(List<Aula> entities);
}