package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private Integer userTargetId;
    private Integer userSourceId;
    private String type;
    private String message;
    private Integer referenceId;
    private String referenceType;
    private LocalDateTime dateSent;
    private boolean isRead;
}
