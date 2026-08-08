package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.api.v1.dto.NotificationRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.NotificationResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.NotificationMapper;
import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.repository.NotificationRepository;
import edu.co.icesi.proyectofinal.repository.UserRepository;
import edu.co.icesi.proyectofinal.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationMapper notificationMapper,
                                   UserRepository userRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotification(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification with id " + id + " not found"));
    }

    @Override
    public Notification saveNotification(Notification notification) {
        if (notification.getDateSent() == null) {
            notification.setDateSent(LocalDateTime.now());
        }

        // Improve automated messages if needed
        if ("NEW_ACTIVITY".equals(notification.getType()) && !notification.getMessage().startsWith("¡")) {
            notification.setMessage("¡Nueva Actividad! Se ha programado: " + notification.getMessage().replace("A new activity has been scheduled: ", ""));
        } else if ("NEW_SPACE".equals(notification.getType()) && !notification.getMessage().startsWith("¡")) {
            notification.setMessage("¡Nuevo Espacio! Se ha habilitado: " + notification.getMessage().replace("A new space has been added: ", ""));
        }

        Notification saved = notificationRepository.save(notification);
        sendNotificationRealTime(saved);
        return saved;
    }

    @Override
    public Notification saveNotificationAPI(NotificationRequest notificationRequest) {
        Notification notification = notificationMapper.toEntity(notificationRequest);

        if (notification.getDateSent() == null) {
            notification.setDateSent(LocalDateTime.now());
        }


        if (notificationRequest.getUserSourceId() == null || notificationRequest.getUserTargetId() == null) {
            throw new IllegalArgumentException("User IDs must not be null");
        }


        User source = userRepository.findById(notificationRequest.getUserSourceId())
                .orElseThrow(() -> new RuntimeException("Source user not found"));
        User target = userRepository.findById(notificationRequest.getUserTargetId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        notification.setUserSource(source);
        notification.setUserTarget(target);
        notification.setDateSent(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        sendNotificationRealTime(saved);
        return saved;
    }

    //Websocket Notification
    private void sendNotificationRealTime(Notification notification) {
        NotificationResponse response = notificationMapper.toResponse(notification);
        if (notification.getUserTarget() != null) {
            log.info("Sending private notification to user: {}", notification.getUserTarget().getInstitutionalEmail());
            messagingTemplate.convertAndSendToUser(
                    notification.getUserTarget().getInstitutionalEmail(),
                    "/queue/notifications",
                    response
            );
        } else {
            log.info("Sending broadcast notification");
            messagingTemplate.convertAndSend("/topic/notifications", response);
        }
    }


    @Override
    public Notification updateNotification(Notification notification) {
        Notification existing = notificationRepository.findById(notification.getIdNotification())
                .orElseThrow(() -> new RuntimeException("Notification with id " + notification.getIdNotification() + " not found"));
        existing.setType(notification.getType());
        existing.setMessage(notification.getMessage());
        existing.setReferenceId(notification.getReferenceId());
        existing.setReferenceType(notification.getReferenceType());
        existing.setDateSent(notification.getDateSent());
        existing.setRead(notification.isRead());

        if (notification.getDateSent() == null) {
            existing.setDateSent(LocalDateTime.now());
        } else {
            existing.setDateSent(notification.getDateSent());
        }

        return notificationRepository.save(existing);
    }

    @Override
    public void deleteNotification(Integer id) {

        if(!notificationRepository.existsById(id)){
            throw new RuntimeException("Notification with id " + id +
                    " not found");
        }

        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> getByUserId(Integer userId) {
        return notificationRepository.findByUserTargetIdUser(userId);
    }

    @Override
    public List<Notification> getUnreadByUserId(Integer userId) {
        return notificationRepository.findByUserTargetIdUserAndIsReadFalse(userId);
    }

    @Override
    public void markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification with id " + id + " not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}