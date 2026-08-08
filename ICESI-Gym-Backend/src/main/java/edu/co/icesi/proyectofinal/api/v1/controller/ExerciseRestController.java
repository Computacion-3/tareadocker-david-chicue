package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.ExerciseRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ExerciseResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.ExerciseMapper;
import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.services.ExerciseService;
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
@RequestMapping("/api/v1/exercises")
@Tag(name = "Exercises", description = "Endpoints for managing fitness exercises")
public class ExerciseRestController {

    private final ExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;

    public ExerciseRestController(ExerciseService exerciseService, ExerciseMapper exerciseMapper) {
        this.exerciseService = exerciseService;
        this.exerciseMapper = exerciseMapper;
    }

    @Operation(
            summary = "Get exercise by ID",
            description = "Retrieves a single exercise's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exercise found", 
                            content = @Content(schema = @Schema(implementation = ExerciseResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Exercise not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LIST_EXERCISE')")
    public ResponseEntity<ExerciseResponse> getById(@PathVariable Integer id) {
        Exercise exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exerciseMapper.toResponse(exercise));
    }

    @Operation(
            summary = "Get all exercises",
            description = "Retrieves a list of all exercises in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of exercises retrieved successfully")
            }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('LIST_EXERCISE')")
    public ResponseEntity<List<ExerciseResponse>> getAll() {
        List<Exercise> exercises = exerciseService.findAll();
        List<ExerciseResponse> exerciseResponses = exercises.stream()
                .map(exerciseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exerciseResponses);
    }

    @Operation(
            summary = "Create a new exercise",
            description = "Creates a new exercise with the provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exercise created", 
                            content = @Content(schema = @Schema(implementation = ExerciseResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_EXERCISE')")
    public ResponseEntity<ExerciseResponse> create(@RequestBody ExerciseRequest exerciseRequest) {
        Exercise exercise = exerciseMapper.toEntity(exerciseRequest);
        Exercise savedExercise = exerciseService.saveExercise(exercise);
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseMapper.toResponse(savedExercise));
    }

    @Operation(
            summary = "Update an existing exercise",
            description = "Updates an existing exercise's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exercise updated successfully", 
                            content = @Content(schema = @Schema(implementation = ExerciseResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Exercise not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_EXERCISE')")
    public ResponseEntity<ExerciseResponse> update(@PathVariable Integer id, @RequestBody ExerciseRequest exerciseRequest) {
        Exercise exercise = exerciseMapper.toEntity(exerciseRequest);
        exercise.setIdExercise(id);
        Exercise updatedExercise = exerciseService.updateExercise(exercise);
        return ResponseEntity.ok(exerciseMapper.toResponse(updatedExercise));
    }

    @Operation(
            summary = "Delete an exercise",
            description = "Deletes an exercise from the system by its ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Exercise deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Exercise not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_EXERCISE')")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
