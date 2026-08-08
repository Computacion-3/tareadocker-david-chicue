package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.AssignmentRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.AssignmentResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.AssignmentMapper;
import edu.co.icesi.proyectofinal.entity.Assignment;
import edu.co.icesi.proyectofinal.entity.keys.AssignmentId;
import edu.co.icesi.proyectofinal.services.AssignmentService;
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
@RequestMapping("/api/v1/assignments")
@Tag(name = "Assignments", description = "Endpoints for managing trainer-user assignments")
public class AssignmentRestController {

    private final AssignmentService assignmentService;
    private final AssignmentMapper assignmentMapper;

    public AssignmentRestController(AssignmentService assignmentService, AssignmentMapper assignmentMapper) {
        this.assignmentService = assignmentService;
        this.assignmentMapper = assignmentMapper;
    }

    @GetMapping
    @Operation(
            summary = "Get all assignments",
            description = "Retrieves a list of all assignments in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of assignments retrieved successfully")
            }
    )
    @PreAuthorize("hasAuthority('LIST_ASSIGNMENT')")
    public ResponseEntity<List<AssignmentResponse>> getAll() {
        return ResponseEntity.ok(assignmentService.findAll().stream()
                .map(assignmentMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/trainer/{trainerId}")
    @Operation(
            summary = "Get assignments by trainer ID",
            description = "Retrieves a list of all assignments for a specific trainer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of assignments retrieved successfully")
            }
    )
    @PreAuthorize("hasAuthority('LIST_ASSIGNMENT')")
    public ResponseEntity<List<AssignmentResponse>> getByTrainerId(@PathVariable Integer trainerId) {
        return ResponseEntity.ok(assignmentService.getByTrainerId(trainerId).stream()
                .map(assignmentMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get assignments by user ID",
            description = "Retrieves a list of all assignments for a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of assignments retrieved successfully")
            }
    )
    @PreAuthorize("hasAnyAuthority('LIST_ASSIGNMENT', 'VIEW_OWN_PROFILE')")
    public ResponseEntity<List<AssignmentResponse>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(assignmentService.getByUserId(userId).stream()
                .map(assignmentMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{userId}/{trainerId}")
    @Operation(
            summary = "Get assignment by user ID and trainer ID",
            description = "Retrieves a single assignment's details by its user ID and trainer ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assignment found", 
                            content = @Content(schema = @Schema(implementation = AssignmentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('LIST_ASSIGNMENT', 'VIEW_OWN_PROFILE')")
    public ResponseEntity<AssignmentResponse> getById(@PathVariable Integer userId, @PathVariable Integer trainerId) {
        AssignmentId id = new AssignmentId();
        id.setUserId(userId);
        id.setTrainerId(trainerId);

        return ResponseEntity.ok(assignmentMapper.toResponse(assignmentService.getById(id)));
    }

    @PostMapping
    @Operation(
            summary = "Create an assignment",
            description = "Creates a new assignment with the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Assignment created", 
                            content = @Content(schema = @Schema(implementation = AssignmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('CREATE_ASSIGNMENT')")
    public ResponseEntity<AssignmentResponse> create(@RequestBody AssignmentRequest request) {
        Assignment assignment = assignmentMapper.toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentMapper.toResponse(assignmentService.save(assignment)));
    }

    @PutMapping("/{userId}/{trainerId}")
    @Operation(
            summary = "Update an assignment",
            description = "Updates an existing assignment's details by its user ID and trainer ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assignment updated successfully", 
                            content = @Content(schema = @Schema(implementation = AssignmentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('EDIT_ASSIGNMENT')")
    public ResponseEntity<AssignmentResponse> update(@PathVariable Integer userId, @PathVariable Integer trainerId, @RequestBody AssignmentRequest request) {
        AssignmentId id = new AssignmentId();
        id.setUserId(userId);
        id.setTrainerId(trainerId);
        Assignment assignment = assignmentMapper.toEntity(request);
        return ResponseEntity.ok(assignmentMapper.toResponse(assignmentService.update(id, assignment)));
    }

    @DeleteMapping("/{userId}/{trainerId}")
    @Operation(
            summary = "Delete an assignment",
            description = "Deletes an assignment from the system by its user ID and trainer ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Assignment deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('DELETE_ASSIGNMENT')")
    public ResponseEntity<Void> delete(@PathVariable Integer userId, @PathVariable Integer trainerId) {

        AssignmentId id = new AssignmentId();
        id.setUserId(userId);
        id.setTrainerId(trainerId);

        assignmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
