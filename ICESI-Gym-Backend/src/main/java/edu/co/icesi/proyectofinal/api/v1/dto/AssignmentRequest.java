package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AssignmentRequest {
    private Integer userId;
    private Integer trainerId;
    private LocalDate assignmentDate;
}
