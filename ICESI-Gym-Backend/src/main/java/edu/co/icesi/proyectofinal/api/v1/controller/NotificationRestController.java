package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.NotificationRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.NotificationResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.NotificationMapper;
import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.services.NotificationService;
import edu.co.icesi.proyectofinal.services.UserService;
import edu.co.icesi.proyectofinal.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "Endpoints for managing system notifications")
public class NotificationRestController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    public NotificationRestController(NotificationService notificationService, NotificationMapper notificationMapper, UserService userService) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all notifications",
            description = "Retrieves a list of all notifications in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of notifications retrieved successfully")
            }
    )
    @PreAuthorize("hasAnyAuthority('LIST_NOTIFICATION', 'VIEW_OWN_PROFILE', 'ADMIN', 'TRAINER', 'TRAINEE')")
    public ResponseEntity<List<NotificationResponse>> getAll(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(notificationService.getNotifications().stream()
                .filter(n -> n.getUserTarget() == null || n.getUserTarget().getIdUser().equals(user.getIdUser()))
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get notification by ID",
            description = "Retrieves a single notification's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification found", 
                            content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('LIST_NOTIFICATION', 'VIEW_OWN_PROFILE', 'ADMIN', 'TRAINER', 'TRAINEE')")
    public ResponseEntity<NotificationResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(notificationMapper.toResponse(notificationService.getNotification(id)));
    }

    @PostMapping
    @Operation(
            summary = "Create a notification",
            description = "Creates a new notification with the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Notification created", 
                            content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('CREATE_NOTIFICATION', 'ADMIN', 'TRAINER')")
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationMapper.toResponse(notificationService.saveNotificationAPI(request)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a notification",
            description = "Updates an existing notification's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification updated successfully", 
                            content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('EDIT_NOTIFICATION', 'EDIT_OWN_PROFILE', 'ADMIN', 'TRAINER', 'TRAINEE')")
    public ResponseEntity<NotificationResponse> update(@PathVariable Integer id, @RequestBody NotificationRequest request) {
        Notification notification = notificationMapper.toEntity(request);
        notification.setIdNotification(id);
        return ResponseEntity.ok(notificationMapper.toResponse(notificationService.updateNotification(notification)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a notification",
            description = "Deletes a notification from the system by its ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Notification deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('DELETE_NOTIFICATION', 'ADMIN', 'TRAINER', 'TRAINEE')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
