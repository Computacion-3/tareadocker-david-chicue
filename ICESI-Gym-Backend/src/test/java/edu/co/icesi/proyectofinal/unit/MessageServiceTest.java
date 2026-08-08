package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Message;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.repository.MessageRepository;
import edu.co.icesi.proyectofinal.services.impl.MessageServiceImpl;
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
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private edu.co.icesi.proyectofinal.api.v1.mapper.MessageMapper messageMapper;

    @Mock
    private org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message message;

    @BeforeEach
    void setUp() {
        User sender = new User();
        sender.setIdUser(1);
        sender.setInstitutionalEmail("sender@icesi.edu.co");

        User receiver = new User();
        receiver.setIdUser(2);
        receiver.setInstitutionalEmail("receiver@icesi.edu.co");

        message = new Message();
        message.setId(1);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Hola!");
        message.setSentAt(LocalDateTime.now());
    }

    @Test
    void getMessages_shouldReturnList() {
        when(messageRepository.findAll()).thenReturn(List.of(message));

        assertEquals(1, messageService.getMessages().size());
    }

    @Test
    void getMessageById_whenExists_shouldReturn() {
        when(messageRepository.findById(1)).thenReturn(Optional.of(message));

        Message result = messageService.getMessageById(1);

        assertNotNull(result);
        assertEquals("Hola!", result.getContent());
    }

    @Test
    void getMessageById_whenNotExists_shouldThrowRuntimeException() {
        when(messageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> messageService.getMessageById(99));
    }

    @Test
    void addMessage_shouldReturnSaved() {
        when(messageRepository.save(message)).thenReturn(message);

        Message result = messageService.addMessage(message);

        assertNotNull(result);
        assertEquals("Hola!", result.getContent());
    }

    @Test
    void updateMessage_whenExists_shouldUpdateAndReturn() {
        Message updated = new Message();
        updated.setId(1);
        updated.setContent("Actualizado");
        updated.setSentAt(message.getSentAt());
        updated.setSender(message.getSender());
        updated.setReceiver(message.getReceiver());

        when(messageRepository.findById(1)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenAnswer(x -> x.getArgument(0));

        Message result = messageService.updateMessage(updated);

        assertEquals("Actualizado", result.getContent());
    }

    @Test
    void updateMessage_whenNotExists_shouldThrowRuntimeException() {
        message.setId(99);
        when(messageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> messageService.updateMessage(message));
    }

    @Test
    void deleteMessage_whenExists_shouldReturnDeletedMessage() {
        when(messageRepository.findById(1)).thenReturn(Optional.of(message));
        doNothing().when(messageRepository).deleteById(1);

        Message result = messageService.deleteMessage(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void deleteMessage_whenNotExists_shouldThrowRuntimeException() {
        when(messageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> messageService.deleteMessage(99));
    }

    @Test
    void getBySenderId_shouldReturnFiltered() {
        when(messageRepository.findBySenderIdUser(1)).thenReturn(List.of(message));

        assertEquals(1, messageService.getBySenderId(1).size());
    }

    @Test
    void getByReceiverId_shouldReturnFiltered() {
        when(messageRepository.findByReceiverIdUser(2)).thenReturn(List.of(message));

        assertEquals(1, messageService.getByReceiverId(2).size());
    }
}