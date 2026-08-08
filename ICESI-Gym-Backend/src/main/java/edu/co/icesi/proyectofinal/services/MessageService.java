package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessages();

    Message getMessageById(Integer id);

    Message addMessage(Message message);

    Message updateMessage(Message message);

    Message deleteMessage(Integer id);

    List<Message> getBySenderId(Integer senderId);

    List<Message> getByReceiverId(Integer receiverId);


}
