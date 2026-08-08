package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.repository.NotificationRepository;
import edu.co.icesi.proyectofinal.services.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private edu.co.icesi.proyectofinal.api.v1.mapper.NotificationMapper notificationMapper;

    @Mock
    private org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        User target = new User();
        target.setIdUser(1);
        target.setInstitutionalEmail("target@icesi.edu.co");

        notification = new Notification();
        notification.setIdNotification(1);
        notification.setUserTarget(target);
        notification.setType("EVENT");
        notification.setMessage("Nuevo evento disponible");
        notification.setReferenceId(10);
        notification.setReferenceType("Activity");
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
    }

    @Test
    void getNotifications_shouldReturnList() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        assertEquals(1, notificationService.getNotifications().size());
    }

    @Test
    void getNotification_whenExists_shouldReturn() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getNotification(1);

        assertNotNull(result);
        assertEquals("EVENT", result.getType());
    }

    @Test
    void getNotification_whenNotExists_shouldThrowRuntimeException() {
        when(notificationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.getNotification(99));
    }

    @Test
    void saveNotification_shouldReturnSaved() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.saveNotification(notification);

        assertNotNull(result);
        assertEquals("EVENT", result.getType());
    }

    @Test
    void updateNotification_whenExists_shouldUpdateAndReturn() {
        Notification updated = new Notification();
        updated.setIdNotification(1);
        updated.setType("ALERT");
        updated.setMessage("Alerta importante");
        updated.setReferenceId(20);
        updated.setReferenceType("Routine");
        updated.setDateSent(LocalDateTime.now());
        updated.setRead(true);

        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(x -> x.getArgument(0));

        Notification result = notificationService.updateNotification(updated);

        assertEquals("ALERT", result.getType());
        assertTrue(result.isRead());
    }

    @Test
    void updateNotification_whenNotExists_shouldThrowRuntimeException() {
        notification.setIdNotification(99);
        when(notificationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.updateNotification(notification));
    }

    @Test
    void deleteNotification_shouldCallRepository() {
        when(notificationRepository.existsById(1)).thenReturn(true);
        doNothing().when(notificationRepository).deleteById(1);
        notificationService.deleteNotification(1);
        verify(notificationRepository).deleteById(1);
    }

    @Test
    void getByUserId_shouldReturnFilteredList() {
        when(notificationRepository.findByUserTargetIdUser(1)).thenReturn(List.of(notification));

        assertEquals(1, notificationService.getByUserId(1).size());
    }

    @Test
    void getUnreadByUserId_shouldReturnOnlyUnread() {
        when(notificationRepository.findByUserTargetIdUserAndIsReadFalse(1)).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getUnreadByUserId(1);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isRead());
    }

    @Test
    void markAsRead_whenExists_shouldSetReadTrue() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(x -> x.getArgument(0));

        notificationService.markAsRead(1);

        assertTrue(notification.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsRead_whenNotExists_shouldThrowRuntimeException() {
        when(notificationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(99));
    }
}