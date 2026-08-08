package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.api.v1.dto.MessageResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.MessageMapper;
import edu.co.icesi.proyectofinal.entity.Message;
import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.repository.MessageRepository;
import edu.co.icesi.proyectofinal.repository.UserRepository;
import edu.co.icesi.proyectofinal.services.MessageService;
import edu.co.icesi.proyectofinal.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageServiceImpl(MessageRepository messageRepository,
                              MessageMapper messageMapper,
                              UserRepository userRepository,
                              NotificationService notificationService,
                              SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message with id " + id + " not found"));
    }

    @Override
    public Message addMessage(Message message) {
        if (message.getSentAt() == null) {
            message.setSentAt(LocalDateTime.now());
        }

        // Fetch full entities to avoid 500 errors (LazyInitialization or missing properties in logger/template)
        if (message.getSender() != null && message.getSender().getIdUser() != null) {
            User sender = userRepository.findById(message.getSender().getIdUser())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            message.setSender(sender);
        }

        if (message.getReceiver() != null && message.getReceiver().getIdUser() != null) {
            User receiver = userRepository.findById(message.getReceiver().getIdUser())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
            message.setReceiver(receiver);
        }

        Message saved = messageRepository.save(message);

        // Create a notification for the receiver
        if (saved.getReceiver() != null) {
            Notification notification = new Notification();
            notification.setType("NEW_MESSAGE");
            notification.setMessage("Has recibido un nuevo mensaje de " + saved.getSender().getFirstName() + " " + saved.getSender().getLastName());
            notification.setReferenceId(saved.getId());
            notification.setReferenceType("MESSAGE");
            notification.setUserTarget(saved.getReceiver());
            notification.setUserSource(saved.getSender());
            notification.setDateSent(LocalDateTime.now());
            notification.setRead(false);
            notificationService.saveNotification(notification);
        }

        sendMessageRealTime(saved);
        return saved;
    }

    //For Websockets
    private void sendMessageRealTime(Message message) {
        MessageResponse response = messageMapper.toResponse(message);
        
        // 1. Send to receiver
        if (message.getReceiver() != null) {
            String receiverEmail = message.getReceiver().getInstitutionalEmail();
            log.info("Sending private message via WS to receiver: {}", receiverEmail);
            messagingTemplate.convertAndSendToUser(
                    receiverEmail,
                    "/queue/messages",
                    response
            );
        }

        // 2. Send back to sender
        if (message.getSender() != null) {
            String senderEmail = message.getSender().getInstitutionalEmail();
            log.info("Sending private message via WS copy back to sender: {}", senderEmail);
            messagingTemplate.convertAndSendToUser(
                    senderEmail,
                    "/queue/messages",
                    response
            );
        }
    }

    @Override
    public Message updateMessage(Message message) {
        Message existing = messageRepository.findById(message.getId())
                .orElseThrow(() -> new RuntimeException("Message with id " + message.getId() + " not found"));
        existing.setContent(message.getContent());
        existing.setSentAt(message.getSentAt());
        existing.setSender(message.getSender());
        existing.setReceiver(message.getReceiver());
        return messageRepository.save(existing);
    }

    @Override
    public Message deleteMessage(Integer id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message with id " + id + " not found"));
        messageRepository.deleteById(id);
        return message;
    }

    @Override
    public List<Message> getBySenderId(Integer senderId) {
        return messageRepository.findBySenderIdUser(senderId);
    }

    @Override
    public List<Message> getByReceiverId(Integer receiverId) {
        return messageRepository.findByReceiverIdUser(receiverId);
    }
}