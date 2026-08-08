package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.api.v1.dto.NotificationRequest;
import edu.co.icesi.proyectofinal.entity.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications();

    Notification getNotification(Integer id);

    Notification saveNotification(Notification notification);

    Notification updateNotification(Notification notification);

    void deleteNotification(Integer id);

    List<Notification> getByUserId(Integer userId);

    List<Notification> getUnreadByUserId(Integer userId);

    void markAsRead(Integer id);

    Notification saveNotificationAPI(NotificationRequest notificationRequest);
    

}