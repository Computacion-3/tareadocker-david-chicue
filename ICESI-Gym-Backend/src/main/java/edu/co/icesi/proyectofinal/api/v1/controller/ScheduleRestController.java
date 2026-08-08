package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.ScheduleRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ScheduleResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.ScheduleMapper;
import edu.co.icesi.proyectofinal.entity.Schedule;
import edu.co.icesi.proyectofinal.services.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedules", description = "Endpoints for managing activity schedules")
public class ScheduleRestController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    public ScheduleRestController(ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }

    @GetMapping
    @Operation(
            summary = "Get all schedules",
            description = "Retrieves a list of all schedules in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully")
            }
    )
    @PreAuthorize("hasAuthority('LIST_SCHEDULE')")
    public ResponseEntity<List<ScheduleResponse>> getAll() {
        return ResponseEntity.ok(scheduleService.getSchedules().stream()
                .map(scheduleMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get schedule by ID",
            description = "Retrieves a single schedule's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Schedule found", 
                            content = @Content(schema = @Schema(implementation = ScheduleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('LIST_SCHEDULE')")
    public ResponseEntity<ScheduleResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(scheduleMapper.toResponse(scheduleService.getSchedule(id)));
    }

    @PostMapping
    @Operation(
            summary = "Create a new schedule",
            description = "Creates a new schedule with the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Schedule created", 
                            content = @Content(schema = @Schema(implementation = ScheduleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('CREATE_SCHEDULE')")
    public ResponseEntity<ScheduleResponse> create(@RequestBody ScheduleRequest request) {
        Schedule schedule = scheduleMapper.toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleMapper.toResponse(scheduleService.saveSchedule(schedule)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing schedule",
            description = "Updates an existing schedule's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Schedule updated successfully", 
                            content = @Content(schema = @Schema(implementation = ScheduleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('EDIT_SCHEDULE')")
    public ResponseEntity<ScheduleResponse> update(@PathVariable Integer id, @RequestBody ScheduleRequest request) {
        Schedule schedule = scheduleMapper.toEntity(request);
        schedule.setIdSchedule(id);
        return ResponseEntity.ok(scheduleMapper.toResponse(scheduleService.updateSchedule(schedule)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a schedule",
            description = "Deletes a schedule from the system by its ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Schedule deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('DELETE_SCHEDULE')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
