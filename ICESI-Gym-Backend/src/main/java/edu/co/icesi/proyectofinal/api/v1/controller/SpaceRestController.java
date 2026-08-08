package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.SpaceRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.SpaceResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.SpaceMapper;
import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.services.SpaceService;
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
@RequestMapping("/api/v1/spaces")
@Tag(name = "Spaces", description = "Endpoints for managing gym spaces")
public class SpaceRestController {

    private final SpaceService spaceService;
    private final SpaceMapper spaceMapper;

    public SpaceRestController(SpaceService spaceService, SpaceMapper spaceMapper) {
        this.spaceService = spaceService;
        this.spaceMapper = spaceMapper;
    }

    @GetMapping
    @Operation(
            summary = "Get all spaces",
            description = "Retrieves a list of all spaces in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of spaces retrieved successfully")
            }
    )
    @PreAuthorize("hasAuthority('LIST_SPACE')")
    public ResponseEntity<List<SpaceResponse>> getAll() {
        return ResponseEntity.ok(spaceService.getSpaces().stream()
                .map(spaceMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get space by ID",
            description = "Retrieves a single space's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Space found", 
                            content = @Content(schema = @Schema(implementation = SpaceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Space not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('LIST_SPACE')")
    public ResponseEntity<SpaceResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(spaceMapper.toResponse(spaceService.getSpace(id)));
    }

    @PostMapping
    @Operation(
            summary = "Create a new space",
            description = "Creates a new space with the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Space created", 
                            content = @Content(schema = @Schema(implementation = SpaceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('CREATE_SPACE')")
    public ResponseEntity<SpaceResponse> create(@RequestBody SpaceRequest request) {
        Space space = spaceMapper.toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(spaceMapper.toResponse(spaceService.createSpace(space)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing space",
            description = "Updates an existing space's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Space updated successfully", 
                            content = @Content(schema = @Schema(implementation = SpaceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Space not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('EDIT_SPACE')")
    public ResponseEntity<SpaceResponse> update(@PathVariable Integer id, @RequestBody SpaceRequest request) {
        Space space = spaceMapper.toEntity(request);
        space.setIdSpace(id);
        return ResponseEntity.ok(spaceMapper.toResponse(spaceService.updateSpace(space)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a space",
            description = "Deletes a space from the system by its ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Space deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Space not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('DELETE_SPACE')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.noContent().build();
    }
}
