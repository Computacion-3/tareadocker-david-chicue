package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.ScheduleRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ScheduleResponse;
import edu.co.icesi.proyectofinal.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    @Mapping(target = "activity.idActivity", source = "activityId")
    Schedule toEntity(ScheduleRequest request);

    @Mapping(target = "activityId", source = "activity.idActivity")
    ScheduleResponse toResponse(Schedule schedule);
}
