package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private LocalDateTime sentAt;
}
