package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.SpaceRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.SpaceResponse;
import edu.co.icesi.proyectofinal.entity.Space;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpaceMapper {
    Space toEntity(SpaceRequest request);
    SpaceResponse toResponse(Space space);
}
