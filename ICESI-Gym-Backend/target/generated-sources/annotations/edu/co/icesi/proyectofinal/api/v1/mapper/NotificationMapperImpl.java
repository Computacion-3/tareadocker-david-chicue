package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.NotificationRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.NotificationResponse;
import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(NotificationRequest request) {
        if ( request == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setUserTarget( notificationRequestToUser( request ) );
        notification.setUserSource( notificationRequestToUser1( request ) );
        notification.setDateSent( request.getDateSent() );
        notification.setMessage( request.getMessage() );
        notification.setRead( request.isRead() );
        notification.setReferenceId( request.getReferenceId() );
        notification.setReferenceType( request.getReferenceType() );
        notification.setType( request.getType() );

        return notification;
    }

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponse.NotificationResponseBuilder notificationResponse = NotificationResponse.builder();

        notificationResponse.userTargetId( notificationUserTargetIdUser( notification ) );
        notificationResponse.userSourceId( notificationUserSourceIdUser( notification ) );
        notificationResponse.dateSent( notification.getDateSent() );
        notificationResponse.idNotification( notification.getIdNotification() );
        notificationResponse.message( notification.getMessage() );
        notificationResponse.referenceId( notification.getReferenceId() );
        notificationResponse.referenceType( notification.getReferenceType() );
        notificationResponse.type( notification.getType() );

        notificationResponse.userSourceName( notification.getUserSource() != null ? notification.getUserSource().getFirstName() + " " + notification.getUserSource().getLastName() : "Icesi Gym" );

        return notificationResponse.build();
    }

    protected User notificationRequestToUser(NotificationRequest notificationRequest) {
        if ( notificationRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( notificationRequest.getUserTargetId() );

        return user;
    }

    protected User notificationRequestToUser1(NotificationRequest notificationRequest) {
        if ( notificationRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( notificationRequest.getUserSourceId() );

        return user;
    }

    private Integer notificationUserTargetIdUser(Notification notification) {
        User userTarget = notification.getUserTarget();
        if ( userTarget == null ) {
            return null;
        }
        return userTarget.getIdUser();
    }

    private Integer notificationUserSourceIdUser(Notification notification) {
        User userSource = notification.getUserSource();
        if ( userSource == null ) {
            return null;
        }
        return userSource.getIdUser();
    }
}
