package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.EnrollmentRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.EnrollmentResponse;
import edu.co.icesi.proyectofinal.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(target = "id.userId", source = "userId")
    @Mapping(target = "id.activityId", source = "activityId")
    @Mapping(target = "user.idUser", source = "userId")
    @Mapping(target = "activity.idActivity", source = "activityId")
    Enrollment toEntity(EnrollmentRequest request);

    @Mapping(target = "userId", source = "id.userId")
    @Mapping(target = "activityId", source = "id.activityId")
    @Mapping(target = "activityName", source = "activity.name")
    @Mapping(target = "activityEndDate", source = "activity.endDate")
    EnrollmentResponse toResponse(Enrollment enrollment);
}
