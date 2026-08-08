package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.ActivityRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ActivityResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.ActivityMapper;
import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.services.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/activities")
@Tag(name = "Activities", description = "Endpoints for managing fitness activities")
public class ActivityRestController {

    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    public ActivityRestController(ActivityService activityService, ActivityMapper activityMapper) {
        this.activityService = activityService;
        this.activityMapper = activityMapper;
    }

    @Operation(
            summary = "Get activity by ID",
            description = "Retrieves a single activity's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Activity found", 
                            content = @Content(schema = @Schema(implementation = ActivityResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Activity not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LIST_ACTIVITY')")
    public ResponseEntity<ActivityResponse> getById(@PathVariable Integer id) {
        Activity activity = activityService.findById(id);
        return ResponseEntity.ok(activityMapper.toResponse(activity));
    }

    @Operation(
            summary = "Get all activities",
            description = "Retrieves a list of all activities in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of activities retrieved successfully")
            }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('LIST_ACTIVITY')")
    public ResponseEntity<List<ActivityResponse>> getAll() {
        List<Activity> activities = activityService.findAll();
        List<ActivityResponse> activityResponses = activities.stream()
                .map(activityMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityResponses);
    }

    @Operation(
            summary = "Create a new activity",
            description = "Creates a new activity with the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Activity created", 
                            content = @Content(schema = @Schema(implementation = ActivityResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ACTIVITY')")
    public ResponseEntity<ActivityResponse> create(@RequestBody ActivityRequest activityRequest) {
        Activity activity = activityMapper.toEntity(activityRequest);
        Activity savedActivity = activityService.save(activity);
        return ResponseEntity.status(HttpStatus.CREATED).body(activityMapper.toResponse(savedActivity));
    }

    @Operation(
            summary = "Update an existing activity",
            description = "Updates an existing activity's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Activity updated successfully", 
                            content = @Content(schema = @Schema(implementation = ActivityResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Activity not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_ACTIVITY')")
    public ResponseEntity<ActivityResponse> update(@PathVariable Integer id, @RequestBody ActivityRequest activityRequest) {
        Activity activity = activityMapper.toEntity(activityRequest);
        activity.setIdActivity(id);
        Activity updatedActivity = activityService.update(activity);
        return ResponseEntity.ok(activityMapper.toResponse(updatedActivity));
    }

    @Operation(
            summary = "Delete an activity",
            description = "Deletes an activity from the system by its ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Activity deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Activity not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ACTIVITY')")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
