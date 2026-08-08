package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.NotificationRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.NotificationResponse;
import edu.co.icesi.proyectofinal.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "userTarget.idUser", source = "userTargetId")
    @Mapping(target = "userSource.idUser", source = "userSourceId")
    Notification toEntity(NotificationRequest request);

    @Mapping(target = "userTargetId", source = "userTarget.idUser")
    @Mapping(target = "userSourceId", source = "userSource.idUser")
    @Mapping(target = "userSourceName", expression = "java(notification.getUserSource() != null ? notification.getUserSource().getFirstName() + \" \" + notification.getUserSource().getLastName() : \"Icesi Gym\")")
    NotificationResponse toResponse(Notification notification);
}
