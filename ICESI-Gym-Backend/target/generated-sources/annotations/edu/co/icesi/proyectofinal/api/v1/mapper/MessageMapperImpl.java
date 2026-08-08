package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.MessageRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.MessageResponse;
import edu.co.icesi.proyectofinal.entity.Message;
import edu.co.icesi.proyectofinal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public Message toEntity(MessageRequest request) {
        if ( request == null ) {
            return null;
        }

        Message message = new Message();

        message.setSender( messageRequestToUser( request ) );
        message.setReceiver( messageRequestToUser1( request ) );
        message.setContent( request.getContent() );
        message.setSentAt( request.getSentAt() );

        return message;
    }

    @Override
    public MessageResponse toResponse(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageResponse messageResponse = new MessageResponse();

        messageResponse.setSenderId( messageSenderIdUser( message ) );
        messageResponse.setReceiverId( messageReceiverIdUser( message ) );
        messageResponse.setContent( message.getContent() );
        messageResponse.setId( message.getId() );
        messageResponse.setSentAt( message.getSentAt() );

        messageResponse.setSenderName( message.getSender() != null ? message.getSender().getFirstName() + " " + message.getSender().getLastName() : null );
        messageResponse.setReceiverName( message.getReceiver() != null ? message.getReceiver().getFirstName() + " " + message.getReceiver().getLastName() : null );

        return messageResponse;
    }

    protected User messageRequestToUser(MessageRequest messageRequest) {
        if ( messageRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( messageRequest.getSenderId() );

        return user;
    }

    protected User messageRequestToUser1(MessageRequest messageRequest) {
        if ( messageRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( messageRequest.getReceiverId() );

        return user;
    }

    private Integer messageSenderIdUser(Message message) {
        User sender = message.getSender();
        if ( sender == null ) {
            return null;
        }
        return sender.getIdUser();
    }

    private Integer messageReceiverIdUser(Message message) {
        User receiver = message.getReceiver();
        if ( receiver == null ) {
            return null;
        }
        return receiver.getIdUser();
    }
}
