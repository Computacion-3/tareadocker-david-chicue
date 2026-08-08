package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Integer idNotification;
    private Integer userTargetId;
    private Integer userSourceId;
    private String userSourceName;
    private String type;
    private String message;
    private Integer referenceId;
    private String referenceType;
    private LocalDateTime dateSent;
    private boolean isRead;
}
