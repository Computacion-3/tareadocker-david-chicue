package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EnrollmentRequest {
    private Integer userId;
    private Integer activityId;
    private LocalDate enrollmentDate;
}
