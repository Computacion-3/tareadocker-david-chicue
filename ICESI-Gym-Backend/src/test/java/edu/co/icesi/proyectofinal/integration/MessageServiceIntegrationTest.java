package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Message;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.repository.MessageRepository;
import edu.co.icesi.proyectofinal.repository.UserRepository;
import edu.co.icesi.proyectofinal.services.MessageService;
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
class MessageServiceIntegrationTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {

        sender = new User();
        sender.setFirstName("Marco");
        sender.setLastName("Rios");
        sender.setInstitutionalEmail("marco.rios@icesi.edu.co");
        sender.setPassword("pass");
        userRepository.save(sender);

        receiver = new User();
        receiver.setFirstName("Diana");
        receiver.setLastName("Vega");
        receiver.setInstitutionalEmail("diana.vega@icesi.edu.co");
        receiver.setPassword("pass");
        userRepository.save(receiver);
    }

    private Message buildMessage(String content) {
        Message m = new Message();
        m.setSender(sender);
        m.setReceiver(receiver);
        m.setContent(content);
        m.setSentAt(LocalDateTime.now());
        return m;
    }

    @Test
    void addMessage_shouldPersist() {
        Message saved = messageService.addMessage(buildMessage("Hola!"));

        assertNotNull(saved.getId());
        assertEquals(1, messageRepository.findAll().size());
    }

    @Test
    void getMessageById_whenExists_shouldReturn() {
        Message saved = messageRepository.save(buildMessage("Test"));

        Message result = messageService.getMessageById(saved.getId());

        assertEquals("Test", result.getContent());
    }

    @Test
    void getMessageById_whenNotExists_shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> messageService.getMessageById(999));
    }

    @Test
    void updateMessage_shouldReflectChanges() {
        Message saved = messageRepository.save(buildMessage("Original"));
        saved.setContent("Actualizado");

        Message result = messageService.updateMessage(saved);

        assertEquals("Actualizado", result.getContent());
    }

    @Test
    void deleteMessage_shouldRemoveAndReturn() {
        Message saved = messageRepository.save(buildMessage("A borrar"));

        Message deleted = messageService.deleteMessage(saved.getId());

        assertNotNull(deleted);
        assertTrue(messageRepository.findAll().isEmpty());
    }

    @Test
    void getMessages_shouldReturnAll() {
        messageRepository.save(buildMessage("M1"));
        messageRepository.save(buildMessage("M2"));

        assertEquals(2, messageService.getMessages().size());
    }

    @Test
    void getBySenderId_shouldReturnFiltered() {
        messageRepository.save(buildMessage("Hola"));

        List<Message> result = messageService.getBySenderId(sender.getIdUser());

        assertEquals(1, result.size());
    }

    @Test
    void getByReceiverId_shouldReturnFiltered() {
        messageRepository.save(buildMessage("Hola"));

        List<Message> result = messageService.getByReceiverId(receiver.getIdUser());

        assertEquals(1, result.size());
    }
}