package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.repository.NotificationRepository;
import edu.co.icesi.proyectofinal.repository.UserRepository;
import edu.co.icesi.proyectofinal.services.NotificationService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User target;
    private User source;

    @BeforeEach
    void setUp() {
        target = new User();
        target.setFirstName("Paula");
        target.setLastName("Leon");
        target.setInstitutionalEmail("paula.leon@icesi.edu.co");
        target.setPassword("pass");
        userRepository.save(target);

        source = new User();
        source.setFirstName("Entrenador");
        source.setLastName("X");
        source.setInstitutionalEmail("trainer.x@icesi.edu.co");
        source.setPassword("pass");
        userRepository.save(source);
    }

    private Notification buildNotification(boolean read) {
        Notification n = new Notification();
        n.setUserTarget(target);
        n.setUserSource(source);
        n.setType("EVENT");
        n.setMessage("Nuevo evento");
        n.setReferenceId(1);
        n.setReferenceType("Activity");
        n.setDateSent(LocalDateTime.now());
        n.setRead(read);
        return n;
    }

    @Test
    void saveNotification_shouldPersist() {
        Notification saved = notificationService.saveNotification(buildNotification(false));

        assertNotNull(saved.getIdNotification());
    }

    @Test
    void getNotification_whenExists_shouldReturn() {
        Notification saved = notificationRepository.save(buildNotification(false));

        Notification result = notificationService.getNotification(saved.getIdNotification());

        assertEquals("EVENT", result.getType());
    }

    @Test
    void getNotification_whenNotExists_shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> notificationService.getNotification(999));
    }

    @Test
    void updateNotification_shouldReflectChanges() {
        Notification saved = notificationRepository.save(buildNotification(false));
        saved.setType("ALERT");
        saved.setRead(true);

        Notification result = notificationService.updateNotification(saved);

        assertEquals("ALERT", result.getType());
        assertTrue(result.isRead());
    }

    @Test
    void deleteNotification_shouldRemove() {
        Notification saved = notificationRepository.save(buildNotification(false));

        notificationService.deleteNotification(saved.getIdNotification());

        assertTrue(notificationRepository.findAll().isEmpty());
    }

    @Test
    void getNotifications_shouldReturnAll() {
        notificationRepository.save(buildNotification(false));
        notificationRepository.save(buildNotification(true));

        assertEquals(2, notificationService.getNotifications().size());
    }

    @Test
    void getByUserId_shouldReturnFiltered() {
        notificationRepository.save(buildNotification(false));

        List<Notification> result = notificationService.getByUserId(target.getIdUser());

        assertEquals(1, result.size());
    }

    @Test
    void getUnreadByUserId_shouldReturnOnlyUnread() {
        notificationRepository.save(buildNotification(false));
        notificationRepository.save(buildNotification(true));

        List<Notification> result = notificationService.getUnreadByUserId(target.getIdUser());

        assertEquals(1, result.size());
        assertFalse(result.get(0).isRead());
    }

    @Test
    void markAsRead_shouldSetReadTrueInDB() {
        Notification saved = notificationRepository.save(buildNotification(false));

        notificationService.markAsRead(saved.getIdNotification());

        Notification updated = notificationRepository.findById(saved.getIdNotification()).orElseThrow();
        assertTrue(updated.isRead());
    }

}