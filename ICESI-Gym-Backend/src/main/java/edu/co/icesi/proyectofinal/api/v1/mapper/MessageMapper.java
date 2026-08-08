package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.MessageRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.MessageResponse;
import edu.co.icesi.proyectofinal.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "sender.idUser", source = "senderId")
    @Mapping(target = "receiver.idUser", source = "receiverId")
    Message toEntity(MessageRequest request);

    @Mapping(target = "senderId", source = "sender.idUser")
    @Mapping(target = "senderName", expression = "java(message.getSender() != null ? message.getSender().getFirstName() + \" \" + message.getSender().getLastName() : null)")
    @Mapping(target = "receiverId", source = "receiver.idUser")
    @Mapping(target = "receiverName", expression = "java(message.getReceiver() != null ? message.getReceiver().getFirstName() + \" \" + message.getReceiver().getLastName() : null)")
    MessageResponse toResponse(Message message);
}
