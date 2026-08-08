package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ScheduleResponse {
    private Integer idSchedule;
    private Integer activityId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
