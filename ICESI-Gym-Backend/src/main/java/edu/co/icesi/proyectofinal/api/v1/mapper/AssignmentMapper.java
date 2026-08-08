package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.AssignmentRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.AssignmentResponse;
import edu.co.icesi.proyectofinal.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    @Mapping(target = "id.userId", source = "userId")
    @Mapping(target = "id.trainerId", source = "trainerId")
    @Mapping(target = "userAssignment.idUser", source = "userId")
    @Mapping(target = "trainerAssignment.idUser", source = "trainerId")
    Assignment toEntity(AssignmentRequest request);

    @Mapping(target = "userId", source = "id.userId")
    @Mapping(target = "trainerId", source = "id.trainerId")
    @Mapping(target = "userFirstName", source = "userAssignment.firstName")
    @Mapping(target = "userLastName", source = "userAssignment.lastName")
    @Mapping(target = "trainerFirstName", source = "trainerAssignment.firstName")
    @Mapping(target = "trainerLastName", source = "trainerAssignment.lastName")
    AssignmentResponse toResponse(Assignment assignment);
}
